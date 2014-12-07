(defproject brew-monitor "0.1.0-SNAPSHOT"
  :description "A web app for monitoring home brews with an
                Internet-enabled thermometer"
  :url "http://github.com/AlarmingCow/brew-monitor"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [liberator "0.11.0"]
                 [compojure "1.1.6"]
		             [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.1.0"]])

