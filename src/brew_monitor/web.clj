(ns brew-monitor.web
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as jetty]
            [hiccup.page :as page]))

(defn index []
  (page/html5
    [:head
      [:title "Hello World"]]
    [:body
      [:div {:id "content"} "Hello World"]]))

(defroutes routes
  (GET "/" [] (index)))

(def application (handler/site routes))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (jetty/run-jetty application {:port port :join? false})))
