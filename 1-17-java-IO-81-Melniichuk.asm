.data
Header db " Result " ,0
 textBuf dd 45 dup(?)
result dd 0
 .code 
main: 

 push  1000000101100000000000000000000b;
 push  1000000010011001100110011001101b;
 push  2;
 pop ebx;
pop eax;
add eax, ebx;
 push eax; 

 pop eax;
cmp eax,0;
xor eax,eax;
setne al;
push eax;

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