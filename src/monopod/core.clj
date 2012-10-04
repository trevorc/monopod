;;; monopod
;;;
;;; Serve a single page site from war resources, falling back to
;;; index.html on GET requests of an unknown resource.
;;;
;;; Non-root deployments are currently unsupported (you must access
;;; your static files with absolute URIs).

(ns ^{:author "Trevor Caira"}
  monopod.core
  (:use [ring.middleware.file-info :only [wrap-file-info]]
        [ring.middleware.resource :only [wrap-resource]])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [ring.util.response :as response])
  (:import java.util.Properties))

(def resource-filename "monopod.properties")

(def config
  (into {} (when-let [resource (io/resource resource-filename)]
             (with-open [rdr (io/reader resource)]
               (doto (Properties.)
                 (.load rdr))))))

(def resource-root
  (get config "root_path" "public"))

(def index-resource
  (get config "index" (string/join \/ [resource-root "index.html"])))

(def error-405-resource
  (get config "error_405"))

(defn handler [{:keys [request-method]}]
  (cond (= request-method :get)
        (response/resource-response index-resource)

        error-405-resource
        (assoc (response/resource-response error-405-resource)
          :status 405)

        :otherwise
        {:status 405 :body "method not allowed"}))

(def application
  (-> handler
      (wrap-resource resource-root)
      (wrap-file-info)))
