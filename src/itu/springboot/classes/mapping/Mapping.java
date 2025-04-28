package itu.springboot.classes.mapping;


import java.util.Iterator;
import java.util.List;

public class Mapping {
   String className;
   Verb verb;

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String var1) {
      this.className = var1;
   }

   public Verb getVerb() {
      return this.verb;
   }

   public void setVerb(Verb var1) {
      this.verb = var1;
   }

   public Mapping() {
   }

   public Mapping(String var1) {
      this.setClassName(var1);
   }

   public Mapping(String var1, Verb var2) {
      this.setClassName(var1);
      this.setVerb(var2);
   }

   public static Mapping getMappingWithVerb(List<Mapping> var0, String var1) {
      Iterator var2 = var0.iterator();

      Mapping var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Mapping)var2.next();
      } while(!var3.getVerb().getVerb().equalsIgnoreCase(var1));

      return var3;
   }

   public static Mapping getMappingWithVerb(Mapping[] var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         Mapping[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Mapping var5 = var2[var4];
            if (var5.getVerb().getVerb().equalsIgnoreCase(var1)) {
               return var5;
            }
         }

         return null;
      }
   }

   public String toString() {
      String var10000 = this.getClassName();
      return "{ className='" + var10000 + "', verbs='" + String.valueOf(this.getVerb()) + "'}";
   }
}
