.586
.model flat, stdcall
option casemap :none ;to distinguish between uppercase and lowercase letters
.stack 4096
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc 
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include module.inc;
.data
Header db " Result " ,0
 textBuf dd 45 dup(?)
result dd 0
a dd 0.0

b dd 0.0

 .code 
main: 

 push  1000000100010011001100110011010b;
pop eax;
mov a,eax
mov eax,a
push eax

pop eax;
cmp eax,0;
je _nextCalculation0
push 1
jmp _endOperation0
_nextCalculation0:

 push  1000000000000000000000000000000b;
pop eax; 
 cmp eax,0;
 xor eax, eax
setne al;
push eax;
_endOperation0:

pop eax;
mov b,eax
mov eax,a
push eax

pop eax;
cmp eax,0;
je _nextCalculation1
push 1
jmp _endOperation1
_nextCalculation1:

 push  1000000000000000000000000000000b;
pop eax; 
 cmp eax,0;
 xor eax, eax
setne al;
push eax;
_endOperation1:

pop eax;
mov b,eax
mov eax,b
push eax

 push  1000000110001100110011001100110b;
 pop ebx;
pop eax;
add eax, ebx;
 push eax; 

pop result
Push Offset textBuf
Push result
call FloatToDec32
invoke MessageBoxA, 0, ADDR textBuf, ADDR Header, 0
ret
Invoke ExitProcess, 0
 end main