(ns brew-monitor.store-es
  (:require [clojurewerkz.elastisch.native :as es]
            [clojurewerkz.elastisch.native.document :as doc]
            [clojurewerkz.elastisch.native.response :as response]))

(def conn (es/connect [["127.0.0.1" 9300]] {"cluster.name" "elasticsearch"}))

(defn most-recent-temps [num]
  (map #(get-in % [:_source :temp])
       (response/hits-from (doc/search conn "temp_gauge" "reading" :sort {:time "desc"} :size num))))
