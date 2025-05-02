/*
 * Copyright (c) 2019 Nuclei Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the License); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/******************************************************************************
 * @file     gcc_Device.ld
 * @brief    GNU Linker Script for gd32vf103 based device
 * @version  V1.0.0
 * @date     17. Dec 2019
 ******************************************************************************/

/*********** Use Configuration Wizard in Context Menu *************************/

OUTPUT_ARCH( "riscv" )
/********************* Flash Configuration ************************************
 * <h> Flash Configuration
 * <o0> Flash Base Address <0x0-0xFFFFFFFF:8>
 * <o1> Flash Size (in Bytes) <0x0-0xFFFFFFFF:8>
 * </h>
 */
__ROM_BASE = 0x08000000;
__ROM_SIZE = 0x00020000;

/*--------------------- Embedded RAM Configuration ---------------------------
 * <h> RAM Configuration
 * <o0> RAM Base Address    <0x0-0xFFFFFFFF:8>
 * <o1> RAM Size (in Bytes) <0x0-0xFFFFFFFF:8>
 * </h>
*/
__RAM_BASE = 0x20000000;
__RAM_SIZE = 0x00005000;

/********************* Stack / Heap Configuration ****************************
 * <h> Stack / Heap Configuration
 * <o0> Stack Size (in Bytes) <0x0-0xFFFFFFFF:8>
 * <o1> Heap Size (in Bytes) <0x0-0xFFFFFFFF:8>
 * </h>
 */
__STACK_SIZE = 0x00000800;
__HEAP_SIZE  = 0x00000800;

/**************************** end of configuration section ********************/

/* Define base address and length of flash and ram */
MEMORY
{
  flash (rxai!w) : ORIGIN = __ROM_BASE, LENGTH = __ROM_SIZE
  ram (wxa!ri) : ORIGIN = __RAM_BASE, LENGTH = __RAM_SIZE
}
/* Linker script to place sections and symbol values.
 * It references following symbols, which must be defined in code:
 *   _Start : Entry of reset handler
 *
 * It defines following symbols, which code can use without definition:
 *   _data_lma
 *   _data
 *   _edata
 *   _bss_start
 *   _end
 *   _sp
 *   __heap_end
 *   __STACK_SIZE
 */
/* Define entry label of program */
ENTRY(_start)
SECTIONS
{
  __STACK_SIZE = DEFINED(__STACK_SIZE) ? __STACK_SIZE : 2K;

  .init           :
  {
    /* vector table locate at flash */
    *(.vtable)
    KEEP (*(SORT_NONE(.init)))
    . = ALIGN(4);
  } >flash AT>flash

  /* Code section located at flash */
  .text           :
  {
    *(.text.unlikely .text.unlikely.*)
    *(.text.startup .text.startup.*)
    *(.text .text.*)
    *(.gnu.linkonce.t.*)
    . = ALIGN(4);
  } >flash AT>flash

  .rodata :
  {
    *(.rdata)
    *(.rodata .rodata.*)
    . = ALIGN(4);
  } >flash AT>flash

  PROVIDE( _data_lma = . );

  /* Define data section virtual address is ram and physical address is flash */
  .data          :
  {
    . = ALIGN(4);
    PROVIDE( _data = . );   
    *(.data .data.*)
    *(.gnu.linkonce.d.*)
    . = ALIGN(8);
    PROVIDE( __global_pointer$ = . + 0x800 );
    *(.sdata .sdata.* .sdata*)
    *(.gnu.linkonce.s.*)
  } >ram AT>flash
  . = ALIGN(4);
  PROVIDE( _edata = . );

  PROVIDE( _bss_start = . );
  .bss            :
  {
    *(.sbss*)
    *(.gnu.linkonce.sb.*)
    *(.bss .bss.*)
    *(.gnu.linkonce.b.*)
    *(COMMON)
    . = ALIGN(4);
  } >ram AT>ram
  . = ALIGN(4);
  PROVIDE( _end = . );

  /* Define stack and head location at ram */
  .stack ORIGIN(ram) + LENGTH(ram) - __STACK_SIZE :
  {
    PROVIDE( _heap_end = . );
    . = __STACK_SIZE;
    PROVIDE( _sp = . );
  } >ram AT>ram
}
