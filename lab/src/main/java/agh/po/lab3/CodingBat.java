package agh.po.lab3;

public class CodingBat {

    /*
    Warump-1 > or35 (https://codingbat.com/prob/p112564):
    
    Return true if the given non-negative number is a multiple of 3 or a multiple of 5. Use the % "mod" operator -- see Introduction to Mod

    Input + output z codingbat:
    or35(3) → true
    or35(10) → true
    or35(8) → false

    */

    public boolean or35(int n) {
        return (n % 3 == 0) || (n % 5 == 0);
    }

    /*
    Warump-1 > close10 (https://codingbat.com/prob/p172021):
    
    Given 2 int values, return whichever value is nearest to the value 10, or return 0 in the event of a tie. Note that Math.abs(n) returns the absolute value of a number.

    Input + output z codingbat:
    close10(8, 13) → 8
    close10(13, 8) → 8
    close10(13, 7) → 0

    */

    public int close10(int a, int b) {
        int a10 = Math.abs(a - 10);
        int b10 = Math.abs(b - 10);
        
        if (a10 < b10) {
            return a;
        }
        if (b10 < a10) {
            return b;
        }
        return 0;
    }

    /*
    Array-2 > fizzBuzz (https://codingbat.com/prob/p153059):
    
    This is slightly more difficult version of the famous FizzBuzz problem which is sometimes given as a first problem for job interviews. 
    (See also: FizzBuzz Code.) Consider the series of numbers beginning at start and running up to but not including end, 
    so for example start=1 and end=5 gives the series 1, 2, 3, 4. Return a new String[] array containing the string form of these numbers, 
    except for multiples of 3, use "Fizz" instead of the number, for multiples of 5 use "Buzz", and for multiples of both 3 and 5 use "FizzBuzz". 
    In Java, String.valueOf(xxx) will make the String form of an int or other type. 
    This version is a little more complicated than the usual version since you have to allocate and index into an array instead of just printing, 
    and we vary the start/end instead of just always doing 1..100.

    Input + output z codingbat:
    fizzBuzz(1, 6) → ["1", "2", "Fizz", "4", "Buzz"]
    fizzBuzz(1, 8) → ["1", "2", "Fizz", "4", "Buzz", "Fizz", "7"]
    fizzBuzz(1, 11) → ["1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz"]

    */

    public String[] fizzBuzz(int start, int end) {
        String[] result = new String[end - start];

            for (int i = start; i < end; i++) {
                if (i % 15 == 0) {
                    result[i - start] = "FizzBuzz";
                } 
                else if (i % 3 == 0) {
                    result[i - start] = "Fizz";
                } 
                else if (i % 5 == 0) {
                    result[i - start] = "Buzz";
                } 
                else {
                    result[i - start] = String.valueOf(i);
                }
            }
        return result;
        }

    /*
    String-1 > startWord (https://codingbat.com/prob/p141494):
        
    Given a string and a second "word" string, we'll say that the word matches the string if it appears at the front of the string, 
    except its first char does not need to match exactly. On a match, return the front of the string, or otherwise return the empty string. 
    So, so with the string "hippo" the word "hi" returns "hi" and "xip" returns "hip". The word will be at least length 1.
    
    Input + output z codingbat:
    startWord("hippo", "hi") → "hi"
    startWord("hippo", "xip") → "hip"
    startWord("hippo", "i") → "h"

    */

    public String startWord(String str, String word) {
        if (str.length() < word.length()) {
            return "";
        }
        
        if (word.length() == 1) {
            return str.substring(0, 1);
        }
        
        if (str.substring(1, word.length()).equals(word.substring(1))) {
            return str.substring(0, word.length());
        }
        
        return "";
    }

}