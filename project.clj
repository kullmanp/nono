(defproject nono "0.1.0-SNAPSHOT"
  :java-source-paths ["java" "test"]
  :junit ["test"]
  :main ch.kup.nono.ui.Main
  :plugins [[lein-auto "0.1.3"]
            [lein-junit "1.1.8"]]
  :auto {:default {:file-pattern #"\.(java)$"}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [commons-io/commons-io "2.6"]]
  :profiles {:dev {:dependencies [[junit/junit "4.12"]]}})
