
 .code 
main: 

 mov eax, 1000011000001110000000000000000b;
 not eax;
 mov ebx, eax; 

 mov eax, 1000000110000000000000000000000b;
 mov ebx, eax; 

 mov eax, 1000000101000000000000000000000b;
 not eax;
 add eax,ebx 

 add eax,ebx 

 not eax;
 return eax;
 end main