class ArchitectureDelegate {
  def list = []

  ArchitectureDelegate() {
  }

  void classes(String name) {
    list.add(name)
  }
  void jar(String name) {
    classes name
  }
  void doit(){
    println list
  }
}