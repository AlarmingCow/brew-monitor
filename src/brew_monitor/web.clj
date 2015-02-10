(ns brew-monitor.web
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as jetty]
            [hiccup.page :as page]
            [brew-monitor.store-es :as store]
            [liberator.core :as lib]))

(defn index []
  (page/html5
    [:head
     [:title "Most recent temperature"]
     (page/include-js "//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js")
     (page/include-js "/js/main.js")]
    [:body
      [:div {:id "content"} (first (store/most-recent-temps))]]))

(defroutes routes
  (GET "/" [] (index))
  (GET "/mostRecentTemp" [] (lib/resource :available-media-types ["application/json"]
                                          :handle-ok { :temp (first (store/most-recent-temps))}))
  (route/resources "/"))

(def application (handler/site routes))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (jetty/run-jetty application {:port port :join? false})))
