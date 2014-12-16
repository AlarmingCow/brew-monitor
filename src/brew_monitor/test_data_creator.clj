(ns brew-monitor.test-data-creator
  (:require [clojurewerkz.elastisch.rest :as es]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clj-time.core :as time]
            [clj-time.format :as time-f]))

(def temps [20.0, 20.1, 20.2, 20.3, 20.5, 20.7, 21.0, 22.0, 21.9, 21.9, 21.8, 19.0, 19.5, 19.8, 19.9])

(def repeated-temps (cycle temps))

(defn get-iso-time []
  (time-f/unparse (time-f/formatters :date-time) (time/now)))

(defn send-temp [temp]
  (let [conn (es/connect "http://localhost:9200")
        doc { :time (get-iso-time), :temp temp }]
    (esd/create conn "temp_gauge" "reading" doc)))

(defn -main []
  (doseq [temp repeated-temps]
    (send-temp temp)
    (Thread/sleep 10000)))
