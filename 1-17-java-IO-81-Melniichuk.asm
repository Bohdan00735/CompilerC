
 .code 
main: 

 push  33;
pop eax;
mov a,eax

 push  33;
pop eax;
mov a,eax
push a
 pop eax;
cmp eax,0;
xor eax,eax;
setne al;
push al;

 push  6;
 pop eax;
cmp eax,0;
xor eax,eax;
setne al;
push al;

 push  5;
 pop ebx;
pop eax;
add eax, ebx;
 push eax; 

 pop ebx;
pop eax;
add eax, ebx;
 push eax; 

 pop eax;
cmp eax,0;
xor eax,eax;
setne al;
push al;

 pop,eax;
ret
 end main