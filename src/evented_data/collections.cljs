(ns evented_data.collections)


(defprotocol IEventSourced
  (diff [this])
  (noun [this])
  (events [this]))


(deftype EventMap [m prior post]
  Object  
  (toString [coll]
    (pr-str* m))
  (equiv [this other]
    (-equiv m other))

  ICloneable
  (-clone [_] (EventMap. m prior post))

  IWithMeta
  (-with-meta [this meta] 
    (EventMap. (-with-meta m meta) prior post))


  IMeta
  (-meta [this] (-meta m))
  
  ICollection
  (-conj [this entry]
    (EventMap. (-conj m entry)
               (conj prior ['conj m entry])
               post))
  
  IEmptyableCollection
  (-empty [coll]
    (EventMap. (-with-meta (.-EMPTY m) (-meta m))
               (.-EMPTY prior)
               (.-EMPTY post)))

  IEquiv
  (-equiv [this x]
    (-equiv m x))

  IHash
  (-hash [this] (-hash m))

  IIterable
  (-iterator [this] (-iterator m))

  ISeqable
  (-seq [this] (-seq m))

  ICounted
  (-count [this] (-count m))
 
  ILookup
  (-lookup [this k]
    (-lookup m k nil))
  (-lookup [this k not-found]
    (-lookup m k not-found))

  IAssociative
  (-contains-key? [this k] (-contains-key? m k))

  (-assoc [this k v] 
    (EventMap. (-assoc m k v)
               (conj prior ['assoc m k v])
               post))

  IMap
  (-dissoc [this k]
    (EventMap. (-dissoc m k)
               (conj prior ['dissoc m k])
               post))
  
  IKVReduce
  (-kv-reduce [this f init]
   (-kv-reduce m f init))

  IReduce
  (-reduce [this f]
    (seq-reduce f this))
  (-reduce [this f start]
    (seq-reduce f start this))

  IFn
  (-invoke [this k]
    (-lookup m k))
  (-invoke [this k not-found]
    (-lookup m k not-found))
  
  IEventSourced
  (diff [this]
    (when-not (empty? prior)
      (EventMap. (second (peek prior))
                 (pop prior)
                 (conj post (peek prior)))))
  (noun [this] m)
  (events [this] post))



(deftype EventVector [v prior post]
  Object
  (toString [coll]
    (pr-str* v))
  (equiv [this other]
    (-equiv v other))
  (indexOf [coll x]
    (-indexOf v x 0))
  (indexOf [coll x start]
    (-indexOf v x start))
  (lastIndexOf [coll x]
    (-lastIndexOf v x (count coll)))
  (lastIndexOf [coll x start]
    (-lastIndexOf v x start))

  ICloneable
  (-clone [_] (EventVector. v prior post))

  IWithMeta
  (-with-meta [coll meta] 
    (EventVector. (-with-meta v meta) prior post))

  IMeta
  (-meta [coll] meta)
  
  IStack
  (-peek [coll] (-peek v))
  (-pop [coll]
    (EventVector. (pop v)
                  (conj prior ['pop v])
                  (.-EMPTY PersistentVector)))

  ICollection
  (-conj [coll o]
    (EventVector. (-conj v o)
                  (conj prior ['conj v o])
                  (.-EMPTY PersistentVector)))
  
  IEmptyableCollection
  (-empty [coll]
    (EventVector. (with-meta (.-EMPTY PersistentVector) (meta v))
                  (.-EMPTY PersistentVector)
                  (.-EMPTY PersistentVector)))

  ISequential
 
  IEquiv
  (-equiv [coll other]
    (if (instance? other EventVector)
       (-equiv v (noun other))
       (-equiv v other)))

  IHash
  (-hash [coll] (-hash v))
  
  ISeqable
  (-seq [coll] (-seq v))

  ICounted
  (-count [coll] (-count v))

  IIndexed
  (-nth [coll n]
    (-nth v n))
  (-nth [coll n not-found]
    (-nth v n not-found))

  ILookup
  (-lookup [coll k]
    (-lookup v k))
  (-lookup [coll k not-found]
    (-lookup v k not-found))
  
  IMapEntry
  (-key [coll]
    (-nth coll 0))
  (-val [coll]
    (-nth coll 1))

  IAssociative
  (-assoc [coll k x]
    (EventVector. (-assoc v k x)
                  (conj prior ['assoc v k x])
                  (.-EMPTY PersistentVector)))

  IReduce
  (-reduce [coll f]
    (-reduce v f))
  (-reduce [coll f init]
    (-reduce v f init))

  IKVReduce
  (-kv-reduce [coll f init]
    (-kv-reduce v f init))

  IFn
  (-invoke [coll k]
    (-nth coll k))
  (-invoke [coll k not-found]
    (-nth coll k not-found))

  IReversible
  (-rseq [coll] (-rseq v))

  IIterable
  (-iterator [this] (-iterator v))

  IEventSourced
  (diff [this]
     (EventVector. (second (peek prior))
                   (pop prior)
                   (conj post (peek prior))))
  (noun [this] v)
  (events [this] post))


(extend-protocol IPrintWithWriter
  EventMap
  (-pr-writer [coll writer opts] (print-map coll pr-writer writer opts))
  EventVector
  (-pr-writer [coll writer opts] (pr-sequential-writer writer pr-writer "[" " " "]" opts coll)))
