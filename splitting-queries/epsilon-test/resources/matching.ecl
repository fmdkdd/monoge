rule foo
match a : EMF!EClass with b : CSV!Row {
  compare {
    return a.name = b.name;  
  }
}