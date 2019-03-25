/*rule rows
match a : ECORE!EClass with b : CSV!Row {
  compare {
    return a.name = b.name;
  }
}*/

rule eclasses
match a : CSV!Row with b : ECORE!EClass {
  compare {
    return a.name = b.name;
  }
}