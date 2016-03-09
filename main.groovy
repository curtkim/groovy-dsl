void run(File dsl) {
  def delegate = new ArchitectureDelegate()
  Script dslScript = new GroovyShell().parse(dsl.text)
  dslScript.metaClass = createEMC(dslScript.class, {ExpandoMetaClass emc ->
    emc.architecture = {Closure cl ->
      cl.delegate = delegate
      cl.resolveStrategy = Closure.DELEGATE_FIRST
      cl()
    }
  })
  dslScript.run()
  delegate.doit()
}

ExpandoMetaClass createEMC(Class clazz, Closure cl) {
  ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
  cl(emc)
  emc.initialize()
  return emc
}

run(new File("architecture.groovy"))