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
     (page/include-js "//d3js.org/d3.v3.min.js")
     (page/include-css "css/main.css")
     (page/include-js "js/main.js")]
    [:body
      [:div {:id "content"}
       [:div {:id "most-recent-temp"} (:temp (first (store/most-recent-temps 1)))]
       [:div {:id "graph"}]]]))

(defroutes routes
  (GET "/" [] (index))
  (GET "/mostRecentTemps" {params :params}
       (lib/resource :available-media-types ["application/json"]
                     :handle-ok {:temps (store/most-recent-temps (Integer/parseInt (:results params)))}))
  (route/resources "/"))

(def application (handler/site routes))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (jetty/run-jetty application {:port port :join? false})))
