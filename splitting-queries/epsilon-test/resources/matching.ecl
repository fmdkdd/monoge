rule foo
match a : CSV!Row with b : ECORE!EClass {
  compare {
    return a.name = b.name;  
  }
}