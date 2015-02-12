(defproject brew-monitor "0.1.0"
  :description "A web app for monitoring home brews with an
                Internet-enabled thermometer"
  :url "http://github.com/AlarmingCow/brew-monitor"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.1.6"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [clojurewerkz/elastisch "2.1.0"]
                 [clj-time "0.8.0"]
                 [liberator "0.12.2"]]
  :plugins [[lein-ring "0.9.1"]]
  :ring {:handler brew-monitor.web/application
         :adapter {:port 8080}
         :auto-refresh? true}
  :main brew-monitor.web)
