(ns brew-monitor.test-data-creator
  (:require [clojurewerkz.elastisch.rest :as es]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clj-time.core :as time]
            [clj-time.format :as time-f]
            [clj-time.local :as local]
            [clj-time.periodic :as periodic]
            [clj-time.coerce :as coerce]))

(def short-temps [20.0, 20.1, 20.2, 20.3, 20.5, 20.7, 21.0, 22.0, 21.9, 21.9, 21.8, 19.0, 19.5, 19.8, 19.9])

(def hour-temps [20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58,
                 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 80, 79, 79, 78, 78, 77, 77, 76, 76,
                 75, 75, 78, 82, 86, 90, 93, 96, 98, 99, 99, 100, 100, 100, 100, 100, 100, 100, 100, 100])

(defn create-reading [time temp]
  {:time (coerce/to-long time), :temp temp})

(defn readings-for-last-hour []
  (let [one-hour-ago (time/minus (local/local-now) (time/hours 1))
        times (periodic/periodic-seq one-hour-ago (time/minutes 1))]
    (map create-reading times hour-temps)))

(def repeated-temps (cycle short-temps))

(defn get-iso-time []
  (time-f/unparse (time-f/formatters :date-time) (time/now)))

(defn send-temp [reading]
  (esd/create (es/connect "http://localhost:9200") "temp_gauge" "reading" reading))

(defn -main []
  (doseq [reading (readings-for-last-hour)]
    (send-temp reading))
  (doseq [temp repeated-temps]
    (send-temp { :time (get-iso-time), :temp temp })
    (Thread/sleep 1000)))
