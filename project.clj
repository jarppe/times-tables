(defproject times-tables "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha9"]
                 [org.clojure/clojurescript "1.9.93"]
                 [reagent "0.6.0-alpha2"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-less "1.7.5"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/css"
                                    "target"]

  :figwheel {:css-dirs ["resources/public/css"]}


  :less {:source-paths ["less"]
         :target-path "resources/public/css"}

  :profiles {:dev {:plugins [[lein-figwheel "0.5.4-3"]
                             [lein-pdo "0.1.1"]]}
             :dist {:clean-targets ^:replace ["dist"]
                    :less {:target-path "dist/css"}}}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/cljs"]
                :figwheel {:on-jsload "times-tables.core/reload"}
                :compiler {:main times-tables.core
                           :output-to "resources/public/js/app.js"
                           :output-dir "resources/public/js/out"
                           :asset-path "js/out"
                           :source-map-timestamp true}}
               {:id "dist"
                :source-paths ["src/cljs"]
                :compiler {:main times-tables.core
                           :output-to "dist/js/app.js"
                           :asset-path "js"
                           :optimizations :advanced
                           :closure-defines {goog.DEBUG false}
                           :pretty-print false}}]}

  :aliases {"dev" ["do"
                   "clean"
                   ["pdo"
                    ["figwheel" "dev"]
                    ["less" "auto"]]]
            "dist" [["with-profile" "dist"]
                    ["do"
                     "clean"
                     ["less" "once"]
                     ["cljsbuild" "once" "dist"]]]})
