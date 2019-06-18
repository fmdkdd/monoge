/*rule rows
match a : ecore!EClass with b : csv!Row {
  compare {
    return a.name = b.name;
  }
}*/

rule eclasses
match a : csv!Row with b : ecore!EClass {
  compare {
    return a.name = b.name;
  }
}
