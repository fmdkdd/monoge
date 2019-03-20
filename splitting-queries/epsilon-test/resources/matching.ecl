rule foo
match a : ECORE!EClass with b : CSV!Row {
  compare {
    return a.name = b.name;  
  }
}