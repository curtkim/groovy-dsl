class TestSuite {
  String name
  def list = []

  TestSuite(String name){
    this.name = name
  }
}


void run(List fileNames) {
  def suites = []

  fileNames.each {file->
    Script dslScript = new GroovyShell().parse(new File(file).text)
    dslScript.metaClass = createEMC(dslScript.class, {ExpandoMetaClass emc ->
      emc.describe = {String name, Closure cl ->
        suites.add(new TestSuite(name))
        cl()
      }
      emc.she = {String name, Closure cl ->
        suites.last().list.add(['name':name, 'closure':cl])
      }
    })
    dslScript.run()
  }

//  println suites.size()
//  println suites.collect {it.name}
//  println suites.first().list

  suites.each {suite->
    println "${suite.name} start"
    suite.list.each {test->
      println "\t${test.name} start"
      test.closure.call()
      println "\t${test.name} end"
    }
    println "${suite.name} end"
  }
}

ExpandoMetaClass createEMC(Class clazz, Closure cl) {
  ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
  cl(emc)
  emc.initialize()
  return emc
}

run(['test1.groovy', 'test2.groovy'])