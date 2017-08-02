(defproject evco-checksum "0.1.0"
  :description "EVCO checksum as described by EMI3"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot evco-checksum.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
