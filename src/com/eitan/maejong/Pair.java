package com.eitan.maejong;

public class Pair<T>
{
   private T first, second;
   
   public Pair(T one, T two) { first = one;  second = two; }
   
   public Pair() {}
   public void set(T one, T two) { first = one;  second = two; }

   public T first() { return first; }
   public T second() { return second; }
   
   public boolean matches()
   {
      return ((Tile) first).matches((Tile) second);
   }
   public void invalidate(MaejongView view)
   {
      ((Tile) first).invalidate(view);
      ((Tile) second).invalidate(view);
   }

   public boolean equals(Object o)
   {
      if (o == null) return false;
      if (!(o instanceof Pair<?>)) return false;
      Pair<?> op = (Pair<?>) o;
      return first.equals(op.first()) && second.equals(op.second());
   }

   public int hashCode()
   {
      return 41 * ( 41 + first.hashCode() ) + second.hashCode(); 
   }
   
}
