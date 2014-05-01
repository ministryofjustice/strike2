(ns strike2.health)

(defn memory-usage[]
  "Return memory usage of JVM"
  (let [runtime (Runtime/getRuntime)
        total_memory (.totalMemory runtime)
        free_memory (.freeMemory runtime)
        mb (* 1024.0 1024.0)]
    {:total_memory (/ total_memory mb)
     :free_memory (/ free_memory mb)
     :used_memory (/ (- total_memory free_memory) mb)
     :max_memory (/ (.maxMemory runtime) mb)}))
