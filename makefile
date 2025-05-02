all: clean example.elf

example.elf: main.o  fun.o  startup.o 
	@echo "linking all the objects"
	riscv-nuclei-elf-gcc.exe -march=rv32i -mabi=ilp32 -nostartfiles -T gcc_flashxip.ld -Wl,-Map=example.map  -o example.elf  startup.o main.o fun.o
	@echo "objdump the elf file"
	riscv-nuclei-elf-objdump.exe  -S  example.elf > example.S.txt
	@echo "generate the hex file"
	riscv-nuclei-elf-objcopy.exe -O ihex example.elf example.hex
	@echo "size evaluation"
	riscv-nuclei-elf-size.exe  example.elf

main.o: main.c
	riscv-nuclei-elf-gcc.exe -march=rv32i -mabi=ilp32 -O1 -g -c -o main.o main.c
fun.o: fun.c
	riscv-nuclei-elf-gcc.exe -march=rv32i -mabi=ilp32 -O1 -g -c -o fun.o fun.c
startup.o: startup.s
	riscv-nuclei-elf-gcc.exe -march=rv32i -mabi=ilp32 -O1 -g -c -o startup.o startup.s

clean:
	rm -f example.elf
