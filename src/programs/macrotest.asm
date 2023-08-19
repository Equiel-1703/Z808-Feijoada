#oi macrodef x
	add ax, x
	sub ax, x
endm

callm oi 1

oi macrodef x
	and ax, x
endm

callm oi 12