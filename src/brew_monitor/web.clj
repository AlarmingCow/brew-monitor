(ns brew-monitor.web
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as jetty]
            [hiccup.page :as page]
            [brew-monitor.store-es :as store]
            [liberator.core :as lib]))

(defn cToF [temp]
  (Math/round (+ (* 9 (/ temp 5)) 32)))

(defn index []
  (page/html5
    [:head
     [:title "Most recent temperature"]
     (page/include-js "//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js")
     (page/include-js "//d3js.org/d3.v3.min.js")
     (page/include-js "//momentjs.com/downloads/moment.js")
     (page/include-css "css/main.css")
     (page/include-js "js/main.js")]
    [:body
      [:div {:id "content"}
       [:div {:id "most-recent-temp"} (str (cToF (:temp (first (store/most-recent-temps 1)))) " ºF")]
       [:div {:id "most-recent-temp-time"} (str "Just now")]
       [:div {:id "graph"}]]]))

(defroutes routes
  (GET "/" [] (index))
  (GET "/mostRecentTemps" {params :params}
       (let [inner-params (-> params
                              (cond-> (:results params) (assoc :max-results (Integer/parseInt (:results params))))
                              (cond-> (:min-time params) (assoc :min-date (Long/parseLong (:min-date params)))))]
         (lib/resource :available-media-types ["application/json"]
                       :handle-ok {:temps (store/most-recent-temps inner-params)})))
  (route/resources "/"))

(def application (handler/site routes))

(defn -main []
  (jetty/run-jetty application {:port 8080}))
