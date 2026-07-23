/** 
Simple utilities for analyzing text files having single-byte encodings.

Reminder: Java's byte data type is a signed data type, -128..127. 
It can't be used for values in the range 0..255.

This package doesn't use or reference character encodings.
It only uses arrays of integers, with values in the range 0..255. 
*/
package encoding.utils;