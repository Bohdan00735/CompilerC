.586
.model flat, stdcall
option casemap :none ;to distinguish between uppercase and lowercase letters
include \masm32\include\windows.inc
include D:\masm32\include\kernel32.inc
include D:\masm32\include\user32.inc
includelib D:\masm32\lib\kernel32.lib
includelib D:\masm32\lib\user32.lib

 .code 
main: 
mov ebx, 367
ret
end main