
 .code 
main: 

 push  135;
 pop eax;
cmp eax,0;
sete al;
push al;

 push  6;
 push  5;
 pop eax;
cmp eax,0;
sete al;
push al;

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
sete al;
push al;

 return eax;
 end main