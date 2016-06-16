;; all the operations must be addField
;; NARS = 2s on AMD64 1.80 GHz 2Gb RAM

(define tv (stv 1 .9))
(define Customer1 (ConceptNode "Customer1" tv))
(define Customer2 (ConceptNode "Customer2" tv))
(define hasAction (PredicateNode "hasAction" tv))
(define addField (ConceptNode "addField" tv))
(define removeField (ConceptNode "removeField" tv))
(define ok (PredicateNode "ok" tv))
(define System (ConceptNode "System" tv))

(define x001 (VariableNode "x001" tv))
(define x002 (VariableNode "x002" tv))


(define first 
  (EvaluationLink 
   hasAction 
   (ListLink 
    Customer2
    addField
    )
   )
  )

(define second 
  (EvaluationLink 
   hasAction
   (ListLink 
    Customer1
    removeField
    )
   )
  )

(define target 
  (ForAllLink 
   tv 
   (ListLink x001)
   (EvaluationLink
    hasAction
    (ListLink x001 addField)
    )
   )
  )

(define target 
  (EvaluationLink
   hasAction
   (ListLink x002 addField)
   )
  )

(pln-bc target 20000)




(define tv (stv 1 0.999))

(define is_axiom (PredicateNode "is_axiom" tv))
(define R (PredicateNode "R" tv))
(define x001 (VariableNode "x001" tv))
(define fact_6 (ConceptNode "fact_6" tv))
(define fact_42 (ConceptNode "fact_42" tv))

(ForAllLink tv (ListLink x001)
    (EvaluationLink
        is_axiom
        (ListLink x001)
    )
)

; tackily use the result, so that FC will know what binding to use
(ImplicationLink tv
    (EvaluationLink
        is_axiom
        (ListLink fact_6)
    )
    (EvaluationLink
        R
        (ListLink fact_42)
    )
)

(define target
    (EvaluationLink
        R
        (ListLink fact_42)
    )
)

; using Inheritance with one rule
(define tv (stv 1 0))
(define Customer1 (ConceptNode "Customer1" tv))
(define Customer2 (ConceptNode "Customer2" tv))
(define hasAction (PredicateNode "hasAction" tv))
(define addField (ConceptNode "addField" tv))
(define removeField (ConceptNode "removeField" tv))
(define ok (PredicateNode "ok" tv))
(define System (ConceptNode "System" tv))
(define x001 (VariableNode "x001" tv))

(InheritanceLink
 tv 
 Customer1
 addField
 )

(define 
  target 
  (InheritanceLink 
   x001
   addField
   )
  )


guile> (pln-bc target 20000)
#<Undefined atom handle>


; Using forAll with EvaluationLink
(define tv (stv 1 0))
(define Customer1 (ConceptNode "Customer1" tv))
(define Customer2 (ConceptNode "Customer2" tv))
(define hasAction (PredicateNode "hasAction" tv))
(define addField (ConceptNode "addField" tv))
(define removeField (ConceptNode "removeField" tv))
(define ok (PredicateNode "ok" tv))
(define System (ConceptNode "System" tv))
(define x001 (VariableNode "x001" tv))

(Forall tv 

(EvaluationLink
 hasAction
 (ListLink Customer2)
 )

; revision demo 

(define oc (ConceptNode "OpenCog" (stv 0.1 0.8)))
(define will_work (ConceptNode "will_work" (stv 0.1 0.8)))
(define ai_plan (ConceptNode "AI_plan" (stv 0.1 0.8)))


(define oc_will_work (InheritanceLink oc will_work))


(InheritanceLink oc ai_plan (stv 0.3 0.7))
(InheritanceLink ai_plan will_work (stv 0.0 0.8))


; revision rule with system ok

(define ok1 (ConceptNode "ok1" (stv 0.1 0.9)))
(define ok2 (ConceptNode "ok2" (stv 0.1 0.9)))
(define ok3 (ConceptNode "ok3" (stv 0.1 0.9)))


(define res (InheritanceLink ok1 ok3))


(InheritanceLink ok1 ok2 (stv 0.2 0.9))
(InheritanceLink ok2 ok3 (stv 0.3 0.9))

(pln-bc res 2000)

