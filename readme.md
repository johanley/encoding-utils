# Simple tools for single-byte text encodings

The classes are mostly independent of each other.


## Example output of Histogram.java

The `Histogram.java` class outputs a summary of the byte-values that occur in a given text.
It's a good place to start when analyzing un-sanitized text.

It looks like this:

```
00(0):0
01(1):0
02(2):0
03(3):0
04(4):0
05(5):0
06(6):0
07(7):0
08(8):0
09(9):0
0A(10):4979
0B(11):0
0C(12):0
0D(13):4979
0E(14):0
0F(15):0
10(16):0
...elided...
FF(255):0

   0 1 2 3 4 5 6 7 8 9 A B C D E F
00| | | | | | | | | | |x| | |x| | 
10| | | | | | | | | | | | | | | | 
20|x|x| |x| | |x| |x|x| |x|x|x|x|x
30|x|x|x|x|x|x|x|x|x|x|x|x| | |x|x
40| |x|x|x|x|x|x|x|x|x|x|x|x|x|x|x
50|x|x|x|x|x|x|x|x|x|x|x|x| |x| | 
60| |x|x|x|x|x|x|x|x|x|x|x|x|x|x|x
70|x|x|x|x|x|x|x|x|x|x|x|x| |x| | 
80| | | | | | | | | | | | | | | | 
90| |x|x|x|x| | | | | | | | | | | 
A0| | | |x| | | | | | | |x| | | | 
B0| | | | | | | | | | | |x| | | | 
C0|x| |x| | | | |x|x|x| | | | | | 
D0| | | | |x| | | | | | | | | | | 
E0|x| |x| | | | |x|x|x|x|x| | |x|x
F0| | |x| |x| |x| | |x| |x| | | | 
Num distinct values: 110
Num distinct values above 126 (7E tilde): 27
Num tab chars: 0
Incompatible with ASCII: [ 91(145) 92(146) ..elided.. ]
Incompatible with 8859-1: [ 91(145) 92(146) 93(147) 94(148) ]
Incompatible with ISOLatin1Encoding (PostScript only): [ ]
Incompatible with Windows-1252: [ ]
```

## Notepad++

Its `Encoding` menu is split into two parts:
- **render** these bytes assuming encoding X
- **attempt to convert** these bytes to encoding Y

The word ANSI may not mean what you think it means:

https://community.notepad-plus-plus.org/topic/27258/standard-ansi-and-code-still-change-to-something-else/2

Be careful of its default behavior:

https://community.notepad-plus-plus.org/topic/24635/how-to-save-file-with-new-encoding/2


