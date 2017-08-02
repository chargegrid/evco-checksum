(ns evco-checksum.core
  (:gen-class)
  (:require [evco-checksum.checksum :as checksum]))

(defn -main [& args]
  (println "Checksum for DE83DUIEN83QGZ = " (checksum/checkdigit "DE83DUIEN83QGZ")))
