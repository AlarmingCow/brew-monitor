(ns brew-monitor.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes ANY]]))

(defroutes app
  (ANY "/" [] (resource :available-media-types ["text/html"]
                        :handle-ok "<h1>This will be something eventually</html>")))

(def handler
  (-> app
      (wrap-params)))

(run-jetty #'handler {:port 3000})
