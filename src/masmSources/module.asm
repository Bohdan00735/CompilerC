.586
.model flat, c
.code
include D:\Bohdan\Assembly\Lab4\Lab4\longop.inc; 

.data
  minimum db 0
  MODULE_local_array_value dd 100 dup(0)
  MODULE_local_r db 0
.code

  ;the StrHex_MY procedure writes the text of the hex code
  ;first parameter is the address of the result buffer (character string)
  ;the second parameter is the address of the number
  ; the third parameter is the number of bits (must be a multiple of 8)
  StrHex_MY proc
  push ebp
  mov ebp,esp
  mov ecx, [ebp+8] ;number of bits of a number
  cmp ecx, 0
  jle @exitp
  shr ecx, 3 ;number of bytes of number
  mov esi, [ebp+12] ;address number
  mov ebx, [ebp+16] ;result buffer address

  @cycle:
  mov dl, byte ptr[esi+ecx-1] ;number byte is two hex digits
  mov al, dl
  shr al, 4 ;senior figure
  call HexSymbol_MY
  mov byte ptr[ebx], al
  mov al, dl ;younger figure
  call HexSymbol_MY
  mov byte ptr[ebx+1], al
  mov eax, ecx
  cmp eax, 4

  jle @next
  dec eax
  and eax, 3 ;the interval divides the groups into eight digits
  cmp al, 0

  jne @next
  mov byte ptr[ebx+2], 32 ;space character code
  inc ebx

  @next:
  add ebx, 2
  dec ecx

  jnz @cycle
  mov byte ptr[ebx], 0 ;the line ends with zero
  @exitp:
  pop ebp
  ret 12
  StrHex_MY endp

  ;this procedure calculates the hex-digit code
  ; parameter - value AL
  ; result -> AL
  HexSymbol_MY proc
  and al, 0Fh
  add al, 48 ;this is only possible for digits 0-9
  cmp al, 58
  jl @exitp
  add al, 7 ;for digits A,B,C,D,E,F
  @exitp:
  ret
  HexSymbol_MY endp
  
  ;this procedure writes 8 HEX character code numbers
  ;The first parameter is a 32-bit number
  ;the second parameter is the address of the text buffer
  DwordToStrHex proc
  push ebp 
  mov ebp,esp
  mov ebx,[ebp+8] ;second parameter
  mov edx,[ebp+12] ;first parameter
  xor eax,eax
  mov edi,7

  @next:
  mov al,dl
  and al,0Fh ;select one hex digit
  add ax,48 ;this is only possible for digits 0-9
  cmp ax,58
  jl @store
  add ax,7 ;for digits A,B,C,D,E,F

  @store:
  mov [ebx+edi],al
  shr edx,4
  dec edi
  cmp edi,0
  jge @next
  pop ebp
  ret 8
  DwordToStrHex endp

HexToDec proc			;write decimal nums
	Push Ebp
    mov ebp,esp
    mov edx, [ebp+8]        ;num of bit
    shr edx, 3                ;bytes
    mov esi, [ebp+12]        ;address of num
    mov edi, [ebp+16]        ;adress of result buffer
    
    mov eax, edx
    shl eax, 2


    mov cl, byte ptr[esi + edx - 1]
    and cl, 128
    cmp cl, 128;check is it negative
    jnz @continue
    mov minimum, 1 ;for negative results
    push edx
    @minus:
    not byte ptr[esi + edx - 1]
    sub edx, 1
    jnz @minus
    inc byte ptr[esi + edx]
    pop edx
    @continue:
    @cycle:       ;in cycle call our conv proc 
    push edx
    call Division_by_Ten_LONGOP
    pop edx
    add bh, 48
    mov byte ptr[edi + eax], bh
    dec eax
    cmp bl, 0
    jz @cycle
    dec edx
    jnz @cycle

    cmp minimum, 1
    jc @nomin
    mov byte ptr[edi + eax + 1], 45
    dec eax
    @nomin:

    inc eax
    @space:
    mov byte ptr[edi + eax], 32
    sub eax, 1
    jnc @space

       
    pop ebp
    ret 12


HexToDec endp

FloatToDecimalMODULE proc

	push ebp 
	mov ebp, esp 
	mov esi, [ebp + 8] 
	mov edi, [ebp + 12] 

	mov eax, esi 
	and eax, 80000000h 
	cmp eax, 0 
	je @end_sign 
	mov byte ptr[edi], 45 
	inc edi 
	
	@end_sign: 
		mov ecx, edi 
		mov eax, esi 
		and eax, 7F800000h 
		shr eax, 23 
		cmp eax, 0 
	jne @next 
	
	mov byte ptr[edi], 48 
	jmp @endproc 
	
	@next: 
		cmp eax, 0FFh 
		jne @next2 
		mov byte ptr[edi], 78 
		mov byte ptr[edi + 1], 65 
		mov byte ptr[edi + 2], 78 
	jmp @endproc 
	
	@next2: 
		sub eax, 7Fh 
		cmp eax, 0 
	jge @next3 
	
	mov byte ptr[edi], 48 
	inc ecx 
	mov ebx, esi 
	and ebx, 7FFFFFh 
	add ebx, 800000h 
	mov edx, 0FFFFFFFFh ;-1 
	imul edx 
	mov edx, ecx 
	mov ecx, eax 
	shr ebx, cl 
	mov ecx, edx 
	jmp @fraction 
	
	@next3: 
		jg @next4 
		mov byte ptr[edi], 49 
		inc ecx 
		mov ebx, esi 
		and ebx, 7FFFFFh 
		jmp @fraction 
	
	@next4: 
		push ecx 
		mov ecx, 23 
		sub ecx, eax 
		push ecx 
		mov eax, esi 
		and eax, 7FFFFFh 
		add eax, 800000h 
		xor ebx, ebx 
		mov ebx, 1 
		shl ebx, cl 
		mov edx, ebx 

	@mask: 
		inc cl 
		shl ebx, 1 
		add ebx, edx 
		cmp cl, 24 
	jne @mask 

	mov edx, eax 
	and edx, ebx 

	mov ebx, eax 
	sub ebx, edx 

	pop ecx 
	shr edx, cl 

	mov eax, 23 
	sub eax, ecx 
	mov ecx, eax 
	shl ebx, cl 

	mov eax, edx 
	pop ecx 
	push ebx 
	mov ebx, 10 
	@full_part: 
		xor edx, edx 
		div ebx 
		add edx, 48 
		mov byte ptr[ecx], dl 
		inc ecx 
		cmp eax, 0 
	jne @full_part 

	mov eax, ecx 
	dec eax 
	@reverse: 
		xor edx, edx 
		mov dh, byte ptr[eax] 
		mov dl, byte ptr[edi] 
		mov byte ptr[eax], dl 
		mov byte ptr[edi], dh 
		inc edi 
		dec eax 
		cmp edi, eax 
	jl @reverse 

	pop ebx 

	@fraction: 
		mov byte ptr[ecx], 44 
		inc ecx 
		mov ax, 6 
	@cycle: 
		shl ebx, 1 
		mov edx, ebx 
		shl edx, 2 
		add ebx, edx 

		mov edx, ebx 
		and edx, 0FF800000h 
		shr edx, 23 
		add dl, 48 
		mov [ecx], dl 
		and ebx, 7FFFFFh 
		inc ecx 
		dec ax 
		cmp ax, 0 
	jne @cycle 
	@endproc: 
		pop ebp 
		ret 8 
FloatToDecimalMODULE endp

StrDec_MY proc
	push ebp
	mov ebp, esp
	
	mov eax, dword ptr[ebp + 16] ; адреса буфера результату
	mov ebx, dword ptr[ebp + 12] ; адреса числа
	mov edx, dword ptr[ebp + 8] ; розрядність у бітах (має бути кратна 8)
	
	push edx
		mov cl, 3
		shr edx, cl
		mov ecx, edx ; ecx - довжина в байтах
	pop edx
	
	
	push edx
	push ecx
	@copy:
		dec ecx
		mov dl, byte ptr[ebx + ecx]
		mov byte ptr[MODULE_local_array_value + ecx], dl
		cmp ecx, 0
		jne @copy
	pop ecx
	pop edx
	
	
	@main_cicle:
		push eax
		push edx
		push ecx
		
			push offset MODULE_local_array_value
			push dword ptr[ebp + 8]
			push offset MODULE_local_array_value
			push offset MODULE_local_r
			call Div10_column_LONGOP
			
		pop ecx 
		pop edx
		pop eax
		
		add MODULE_local_r, 48; перевод ASCII 
		mov dl, MODULE_local_r
		mov byte ptr[eax], dl
		inc eax
		
		push ecx
		xor dl, dl; счетчік ненульових байтів
		@zero_check:
			dec ecx
			cmp byte ptr[MODULE_local_array_value + ecx], 0
			je @zero
				inc dl
				jmp @zero_checkEnd; найдений 1 не нульовий (більше і не потрібно)
			@zero:
			cmp ecx, 0
			jne @zero_check
		@zero_checkEnd:
		pop ecx
		
		cmp dl, 0
		jne @main_cicle ; якщо є хоч один не нульовий байт то продовжуємо ділити на 10
	
	mov byte ptr[eax], 0
	dec eax
	mov ecx, dword ptr[ebp + 16]
	@reverse:
		mov bl, byte ptr[eax]
		mov bh, byte ptr[ecx]
		mov byte ptr[eax], bh
		mov byte ptr[ecx], bl
		dec eax
		inc ecx
		cmp ecx, eax
		jl @reverse
	
	@exit:
	pop ebp
	ret 12

StrDec_MY endp

End