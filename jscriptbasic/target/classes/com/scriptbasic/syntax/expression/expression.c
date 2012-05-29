/*
FILE:   expression.c
HEADER: expression.h

--GNU LGPL
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

TO_HEADER:

#define MAX_LEXES_PER_LINE 14 // the maximum number of lexicals allowed on a line
                              // (note that an expression or an expression list is one lexical in this calculation)
#define MAX_GO_CONSTANTS 3    // the number of different unnamed label types a command may accept
                              // usually the only such construction is ENDIF that can finish an ELSE as
                              // well as an IF branch. But to be safe we have three.
#define MAX_SAME_LABELS 10    // the max number of labels that can point to the same line

typedef struct _SymbolUF { //User Function
  unsigned long FunId; // The serial number of the function
  long Argc; // the number of arguments (-1 means uninitialized)
  unsigned long node; // where the function is defined
  char *FunctionName; // the name of the function to print in error message when it is used, but not defined
  struct _SymbolUF *next;
  } SymbolUF, *pSymbolUF;


typedef struct _eNODE {
  long OpCode; // the code of operation
  unsigned long NodeId; // the id of the node
  char *szFileName;// where the lexeme is
  long lLineNumber;// from which this syntax node is made
  union {

    // when the node is a command
    struct {
      union {
        struct _SymbolLABEL *pLabel;
        struct _eNODE *pNode;
        struct _eNODE_l *pNodeList;
        long lLongValue;
        double dDoubleValue;
        char *szStringValue;
        }Argument;
      long sLen;
      struct _eNODE *next;
      }CommandArgument;

    // when the node is an operation
    struct {
      struct _eNODE_l *Argument;
      }Arguments;

    // when the node is a constant
    struct {
      union {
        double dValue;        
        long   lValue;        
        char  *sValue;
        }Value;
      long sLen; //the length of the string constant
      }Constant;

    // when the node is a variable
    struct {
      unsigned long Serial; // the serial number of the variable
      }Variable;

    // when node is a user functions
    struct {
      pSymbolUF pFunction; // pointer to the function
      struct _eNODE_l *Argument;
      }UserFunction;

    }Parameter;

  } eNODE,*peNODE;

// these values are used for the built-in functionalities
// other opcode values are defined in tables
enum {
  eNTYPE_ARR=1, // array access
  eNTYPE_SAR,   // assoc array access
  eNTYPE_FUN,   // function
  eNTYPE_LVR,   // local variable
  eNTYPE_GVR,   // global variable
  eNTYPE_DBL,   // constant double
  eNTYPE_LNG,   // constant long
  eNTYPE_STR,   // constant string
  eNTYPE_LST,   // list member (only after build)
  eNTYPE_CRG,   // command arguments

  __eNTYPE_DUMMY__
  };

// node list
typedef struct _eNODE_l{
  unsigned long NodeId; // the id of the node
  char *szFileName;// where the lexeme is
  long lLineNumber;// from which this syntax node is made
  peNODE actualm;
  struct _eNODE_l *rest;
  } eNODE_l, *peNODE_l;

typedef struct _SymbolLABEL { // label for GOTO and alikes
  long Serial; // serial value of the label
  unsigned long node; // where the label is placed (the node id)
  } SymbolLABEL, *pSymbolLABEL;

typedef struct _SymbolVAR { //Variable
  long Serial; // serial number of the variable
  } SymbolVAR, *pSymbolVAR;

typedef struct _LabelStack {
  pSymbolLABEL pLabel;
  struct _LabelStack *Flink;
  long Type;
  } LabelStack, *pLabelStack;

typedef struct _BFun {   // built in function
  long OpCode;  // Lexeme symbol code as well as operation code
  long MinArgs; // The minimum nulber of arguments
  long MaxArgs; // The maximum number of arguments
  } BFun, *pBFun;

typedef struct _PredLConst {
  char *name;
  long value;
  }PredLConst,*pPredLConst;

// constant values used in the line syntax description table
enum {
  EX_LEX_EXP = 1,  // expression
  EX_LEX_EXPL,     // expression list
  EX_LEX_LVAL,     // left value
  EX_LEX_LVALL,    // left value list
  EX_LEX_NSYMBOL,  // non-alpha symbol or alpha symbol which is reserved and therefore tokenized
  EX_LEX_SYMBOL,   // a symbol, like a variable
  EX_LEX_ASYMBOL,  // a symbol that does not need name space tuning (usually like MODULE xxx)
  EX_LEX_PRAGMA,   // a pragma symbol
  EX_LEX_SET_NAME_SPACE, // a symbol that sets the name space
  EX_LEX_RESET_NAME_SPACE, // end of name space, old name space is restored
  EX_LEX_CHARACTER,// a character, like '(' or ')'
  EX_LEX_LONG,     // a numeric integer value
  EX_LEX_DOUBLE,   // a numeric float value
  EX_LEX_STRING,   // a string value
  EX_LEX_LOCAL,    // a local variable, does not generate code, but is declared as local
  EX_LEX_LOCALL,   // local variable list,    -"-
  EX_LEX_GLOBAL,   // global variable, -"-
  EX_LEX_GLOBALL,  // global variable list , -"-
  EX_LEX_FUNCTION, // function or procedure definition symbol
  EX_LEX_THIS_FUNCTION, // the name of the current function
  EX_LEX_LABEL_DEFINITION, //a global label is defined (symbol)
  EX_LEX_LABEL,    // a global label is used
  EX_LEX_STAR,     // the star pseudo lexer that says the syntax failure is final
  EX_LEX_NOEXEC,   // the command needs no code
  EX_LEX_ARG_NUM,  // store the number of arguments

  EX_LEX_GO_FORWARD,
  EX_LEX_GO_BACK,
  EX_LEX_COME_FORWARD,
  EX_LEX_COME_BACK,

// The followings are pseudo syntax elements that instruct the
// compiler to do some semantic action, but does not actually
// match any lexical elements.
// These values should usually appear at the end of a line
// definition after the new line.

  EX_LEX_LOCAL_START, // start a new local area, like start of a user proc or user function
  EX_LEX_LOCAL_END,   // finish a local area

  EX_LEX_CONST_NAME,  // constant name
  EX_LEX_GCONST_NAME, // global constant name
  EX_LEX_CONST_VALUE, // constant value

  EX_LEX_DUMMY
  };

typedef struct _LineSyntaxUnit {
  int type;          // type of the syntactical element from the enum above
  long OpCode;       // the opcode
  long GoConstant[MAX_GO_CONSTANTS];
  } LineSyntaxUnit, *pLineSyntaxUnit;

typedef struct _LineSyntax {
  long CommandOpCode;
  peNODE (*pfAnalyzeFunction)();
  LineSyntaxUnit lexes[MAX_LEXES_PER_LINE];
  } LineSyntax, *pLineSyntax;

typedef struct _NameSpaceStack {
  struct _NameSpaceStack *next;
  char *ThisNameSpace;
  }NameSpaceStack, *pNameSpaceStack;

typedef struct _eXobject {
  void *(*memory_allocating_function)(size_t);
  void (*memory_releasing_function)(void *);
  void *pMemorySegment; //this pointer is passed to the memory allocating functions
                        //this pointer is initialized in ex_init
  void *pLocalVarMemorySegment; // this memory segment is used to allocate local memory variables
                                // initialized in ex_init
  void *pSymbolTableMemorySegment; // for all symbol table entries that are not local

  pLexObject pLex;    // the lexicals that we work up

  SymbolTable GlobalVariables;
  SymbolTable UserFunctions;
  SymbolTable LocalVariables;
  SymbolTable LocallyDeclaredGlobalVariables;
  SymbolTable GlobalLabels;  // currently there are only global labels, locality is done with name decoration
  SymbolTable GlobalConstants; // global constants, locality is done with name decoration
  long *plNrLocalVariables; //pointer to the long where we store the number of the local variables
                            // this value is known at the end of the user function or user proc,
                            // but should be stored in the func or proc head.
                            // Note that we do need a single variable for this, and not a push/pop stack
                            // because functions and procedures can not be nested
  pPredLConst PredeclaredLongConstants;
  long cGlobalVariables;
  long cGlobalLabels;
  long cLocalVariables;
  long cUserFunctions;

  pSymbolUF FirstUF; // the first User Function. Used to go throug the functions to check that all are defined.

  int iWeAreLocal; // is true when variables should be processed as local variables otherwise zero
  int iDeclareVars; // is true when all global/local variables have to be declared
  int iDefaultLocal; // it is true when all undefined variables are treated as local

// ScriptBasic supports modules and nested name spaces. However
// this is nothing else than allowing :: in names, like main::a.

// There is always a current name space during compilation. The default
// name space is main.

// A symbol or variable in the source can be absolute or relative
// regarding name spaces. An absolute reference is like T<main::v>
// a variable named T<v> from the name space main. Name spaces can
// arbitrarily be nested, therefore package::subpackage::v is a valid variable.

// The variable CurrentNameSpace contains the current name space and
// should always contain the trailing ::


  char *CurrentNameSpace;
  long cbCurrentNameSpace;
  pNameSpaceStack pOldNameSpace;

  unsigned long *Unaries; // array of token codes that are unary operations. Final element should be zero
  // This array contains the opcodes of the binary operations. Each odd element should give the opcode
  // of the operation and the next element is the precedence value. The final element is zero,
  // therefore zero can not be used as a legal opcode for binary operations.
  unsigned long *Binaries;
  unsigned long MAXPREC; // maximal precedence of binary operators

  pBFun BuiltInFunctions;

  pReportFunction report;
  void *reportptr; // this pointer is passed to the report function. The caller should set it.
  int iErrorCounter;
  unsigned long fErrorFlags;

  char *Buffer;
  size_t cbBuffer;

  pLineSyntax Command;

  peNODE_l pCommandList;

  unsigned long NodeCounter; // used to count the nodes and assign NodeId

  pSymbolUF ThisFunction; // the serial number of the current function

  // the stack to store the unnamed labels for loop and 'if then else' constructions
  pLabelStack pComeAndGoStack;
  pLabelStack pFreeComeAndGoStack; // to save global allocation we store freed label stack structures
                                   // in this list
  pSymbolLABEL LabelsWaiting[MAX_SAME_LABELS];
  unsigned long cLabelsWaiting;
  pSymbolUF pFunctionWaiting;

  unsigned long cbStringTable; // all the bytes of builder StringTable including the zeroes
  struct _PreprocObject *pPREP;
  } eXobject, *peXobject;

typedef void (*CommandFunctionType)();

*/

/*POD

The functions in this file compile a ScriptBasic expression into
internal form. The functions are quite general, and do NOT depend
on the actual operators that are implemented in the actual version.

This means that you can define extra operators as well as extra
built-in functions easily adding entries into tables and need not modify
the compiling code.

CUT*/

/*POD
=H What is an expression in ScriptBasic

Altough the syntax defintion in script basic is table driven and can easily be modified
expressions are not. The syntax of an expression is somewhat fix. Here we formally define
what the program thinks to be an expression. This restriction should not cause problem
in the usage of this module because this is the usual syntax of an expression. Any altering
to this would result in an expression syntax which is unusual, and therefore difficult to
use for the common users. The operators and functions along with therir precedence values are
defined in tables anyway, so you have flexibility.

The formal description of an expression syntax:

=verbatim
 tag ::= UNOP tag
         NUMBER
         STRING
         '(' expression ')'
         VARIABLE { '[' expression_list ']' }
         VARIABLE '{' expression_list '}'
         FUNC '(' expression_list ')'
         .

 expression_list ::= expression [ ',' expression_list ] .

 expression_i(1) ::= tag .

 expression_i(i) := expression_i(i-1) [ OP(i) expression_i(i) ] .

 expression ::= expression_i(MAX_PREC) .

 left_value ::= variable { '[' expression_list ']' } 
                variable '{' expression_list '}' .

=noverbatim

=itemize
=item UNOP

is unary operator as defined in tables in file operators.h

=item NUMBER 

is a number, lexical element.

=item STRING 

is a string, lexical element.

=item VARIABLE 

is a lexical element.

=item FUNC 

is a function either built in, or user defined

=item OP(i)

is an operator of precendece i as defined in tables.

=noitemize


CUT*/
#include <stdlib.h>
#include <string.h>
#include <stdio.h>


#include "errcodes.h"
#include "report.h"
#include "lexer.h"
#include "sym.h"
#include "expression.h"
#include "myalloc.h"

/* we need this to get the constant CMD_EQ, CMD_MINUS, CMD_PLUS */
#include "syntax.h"
#include "ipreproc.h"

#define new_eNODE()    _new_eNODE(pEx)
#define new_eNODE_l()  _new_eNODE_l(pEx,NULL,0L)
#define new_eNODE_lL() _new_eNODE_l(pEx,pszFileName,lLineNumber)
#define new_SymbolUF() _new_SymbolUF(pEx)
#define new_SymbolVAR(y) _new_SymbolVAR(pEx,y)
#define new_SymbolLABEL() _new_SymbolLABEL(pEx)
#define ex_PopLabel(y) _ex_PopLabel(pEx,y)
#define ex_CleanLabelStack() _ex_CleanLabelStack(pEx)
#define LOCAL_VAR 1
#define GLOBAL_VAR 0

#define LexemeLineNumber lex_LineNumber(pEx->pLex)
#define LexemeFileName   lex_FileName(pEx->pLex)
#define NextLexeme       lex_NextLexeme(pEx->pLex);
#define LexemeType       (lex_EOF(pEx->pLex) ? 0 : lex_Type(pEx->pLex))
#define LexemeCode       lex_Code(pEx->pLex)
#define LexemeChar       lex_Char(pEx->pLex)
#define LexemeStrLen     lex_StrLen(pEx->pLex)
#define LexemeString     lex_String(pEx->pLex)
#define LexemeDouble     lex_Double(pEx->pLex)
#define LexemeLong       lex_Long(pEx->pLex)
#define LexemeInt        lex_Int(pEx->pLex)
#define LexemeSymbol     lex_Symbol(pEx->pLex)
#define WeAreLocal       (pEx->iWeAreLocal)
#define WeAreNotLocal    (!pEx->iWeAreLocal)
#define DeclareVars   (pEx->iDeclareVars)
#define DefaultLocal     (pEx->iDefaultLocal)

#define COUNT_STRING_LEN (pEx->cbStringTable += sizeof(long));

#define CALL_PREPROCESSOR(X,Y) if( pEx->pPREP && pEx->pPREP->n )ipreproc_Process(pEx->pPREP,X,Y)

#define REPORT(x1,x2,x3,x4) do{if( pEx->report )pEx->report(pEx->reportptr,x1,x2,x3,REPORT_ERROR,&(pEx->iErrorCounter),x4,&(pEx->fErrorFlags));}while(0)

static isinset(int ch,char *string){
   while( ch != *string && *++string );
   return *string;
}

static void _ex_printVAR(char *name, void *value, void *f){
  FILE *fp = (FILE *)f;
  pSymbolVAR p = (pSymbolVAR)value;

  fprintf(f,"%s=%d\n",name,p->Serial);
  }

/*POD
=H ex_DumpVariables()

This function dumps the variables stored in the symbol table to the file pointed by
T<fp>

/*FUNCTION*/
void ex_DumpVariables(SymbolTable q,
                      FILE *fp
  ){
/*noverbatim

Note that this function is a debug function.
CUT*/
  sym_TraverseSymbolTable(q,_ex_printVAR,(void*)fp);
  }

static void _ex_pprint(FILE *f, peNODE p, peXobject pEx,int tab);
void _ex_pprint_l(FILE *f, peNODE_l p, peXobject pEx,int tab){
  fprintf(f,"%*sexpression list\n",tab,"");
  while( p ){
    fprintf(f,"%*sNode id=%d\n",tab,"",p->NodeId);
    _ex_pprint(f,p->actualm,pEx,tab+1);
    p = p->rest ;
    }
  }

static void _ex_pprint(FILE *f, peNODE p, peXobject pEx,int tab){
  unsigned long *q;
  pLineSyntax pCommand;
  peNODE_l z;
  int i,j;

#define OPCODE (p->OpCode)

  if( tab )fprintf(f,"%*s",tab,"");
  fprintf(f," %d ",p->NodeId);
  tab++;
  if( p == NULL  )return;
  switch(OPCODE){

    case eNTYPE_ARR: /* array access */
      fprintf(f,"Array access\n");
      _ex_pprint_l(f, p->Parameter.Arguments.Argument ,pEx,tab+1);
      break;
    case eNTYPE_SAR: /* associative array access */
      fprintf(f,"Associative array access\n");
      _ex_pprint_l(f, p->Parameter.Arguments.Argument ,pEx,tab+1);
      break;
    case eNTYPE_FUN: /* function */
      fprintf(f,"User function call starting at node %d\n",p->Parameter.UserFunction.pFunction->node);
      _ex_pprint_l(f, p->Parameter.UserFunction.Argument ,pEx,tab+1);
      break;
    case eNTYPE_LVR: /* local variable */
      fprintf(f,"Local variable %d\n",p->Parameter.Variable.Serial);
      return;
    case eNTYPE_GVR: /* global variable */
      fprintf(f,"Global variable %d\n",p->Parameter.Variable.Serial);
      return;
    case eNTYPE_DBL: /* constant double */
      fprintf(f,"Double: %f\n",p->Parameter.Constant.Value.dValue);
      return;
    case eNTYPE_LNG: /* constant long */
      fprintf(f,"Long: %d\n",p->Parameter.Constant.Value.lValue);
      return;
    case eNTYPE_STR: /* constant string */
      fprintf(f,"String %s\n",p->Parameter.Constant.Value.sValue);
      return;
    default: /* */
      q = pEx->Binaries;

      while( *q && *q != (unsigned)OPCODE )q+=2;
      if( *q ){
        fprintf(f,"Opcode: %d %s",OPCODE,lex_SymbolicName(pEx->pLex,OPCODE));
        fprintf(f,"bin\n");
        _ex_pprint(f,p->Parameter.Arguments.Argument->actualm,pEx,tab);
        fprintf(f,"rest %d\n",p->Parameter.Arguments.Argument->rest->NodeId);
        _ex_pprint(f,p->Parameter.Arguments.Argument->rest->actualm,pEx,tab);
        return;
        }
      q = pEx->Unaries;
      while( *q && *q != (unsigned)OPCODE )q++;
      if( *q ){
        fprintf(f,"Opcode: %d %s",OPCODE,lex_SymbolicName(pEx->pLex,OPCODE));
        fprintf(f,"una\n");
        _ex_pprint(f,p->Parameter.Arguments.Argument->actualm,pEx,tab);
        return;
        }
      pCommand = pEx->Command;
      while( pCommand && pCommand->CommandOpCode != 0 && pCommand->CommandOpCode != OPCODE )pCommand++;
      if( pCommand && pCommand->CommandOpCode ){
        fprintf(f,"Command %d %s\n",OPCODE,lex_SymbolicName(pEx->pLex,OPCODE));
        for( i=0,j=0 ; j < MAX_LEXES_PER_LINE && pCommand->lexes[j].type && p ; j++ ){
          switch( pCommand->lexes[j].type ){
            case EX_LEX_CHARACTER:
              break;
            case EX_LEX_NSYMBOL:
              break;
            case EX_LEX_EXP:
              fprintf(f,"%*sexpression %d\n",tab,"",p->Parameter.CommandArgument.Argument.pNode->NodeId);
              _ex_pprint(f,p->Parameter.CommandArgument.Argument.pNode,pEx,tab);
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_EXPL:
              fprintf(f,"%*sexpression list\n",tab,"");
              z = p->Parameter.CommandArgument.Argument.pNodeList;
              while( z ){
                _ex_pprint(f,z->actualm,pEx,tab);
                z = z->rest;
                }
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_LVAL:
              fprintf(f,"%*slval %d\n",tab,"",p->Parameter.CommandArgument.Argument.pNode->NodeId);
              _ex_pprint(f,p->Parameter.CommandArgument.Argument.pNode,pEx,tab);
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_SYMBOL:
              fprintf(f,"%*ssymbol=%s\n",tab,"",p->Parameter.CommandArgument.Argument.szStringValue);
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_LONG:
              fprintf(f,"%*slong=%d\n",tab,"",p->Parameter.CommandArgument.Argument.lLongValue);
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_DOUBLE:
              fprintf(f,"%*sdouble=%f\n",tab,"",p->Parameter.CommandArgument.Argument.dDoubleValue);
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_ASYMBOL:
              fprintf(f,"%*ssymbol=\"%s\"\n",tab,"",p->Parameter.CommandArgument.Argument.szStringValue);
              p=p->Parameter.CommandArgument.next;
              break;
            case EX_LEX_STRING:
              fprintf(f,"%*sstring=\"%s\"\n",tab,"",p->Parameter.CommandArgument.Argument.szStringValue);
              p=p->Parameter.CommandArgument.next;
              break;
            }       
          }
        return;
        }
      /* should be built-in function */
      fprintf(f,"Opcode: %d ",OPCODE);
      fprintf(f,"bif\n");
      z = p->Parameter.Arguments.Argument;
      while( z ){
         _ex_pprint(f,z->actualm,pEx,tab);
         z = z->rest;
         }
      return;
     }
#undef OPCODE
   }

/*POD
=H expression_PushNameSpace()

When a T<module name> instruction is encountered the name space is modified. However
the old name space should be reset when an T<end module> statement is reached. As the
modules can be nested into each other the name spaces are stored in a name space stack
during syntax analysis.

This function pushes the current name space onto the stack. After calling
this function the caller can put the new string into the T<pEx->>T<CurrentNameSpace>
variable and later calling R<ex_PopNameSpace()> can be called to retrive the saved name space.

/*FUNCTION*/
int expression_PushNameSpace(peXobject pEx
  ){
/*noverbatim
CUT*/
  pNameSpaceStack p;

  p = (pNameSpaceStack)alloc_Alloc(sizeof(NameSpaceStack),pEx->pMemorySegment);
  if( p == NULL )return EX_ERROR_MEMORY_LOW;

  p->ThisNameSpace = (char *)alloc_Alloc(strlen(pEx->CurrentNameSpace)+1,pEx->pMemorySegment);
  if( p->ThisNameSpace == NULL ){
    alloc_Free(p,pEx->pMemorySegment);
    return EX_ERROR_MEMORY_LOW;
    }
  strcpy(p->ThisNameSpace,pEx->CurrentNameSpace);

  p->next = pEx->pOldNameSpace;
  pEx->pOldNameSpace = p;
  return EX_ERROR_SUCCESS;
  }

/* This is a callback function that the function CheckUndefinedLabels
   function calls via the TraverseSymbolTable function.
*/
static void CUL_callback(char *LabelName, void *pL, void *f){
  peXobject pEx = (peXobject)f;
  pSymbolLABEL pLabel = (pSymbolLABEL)pL;

  if( pLabel->node == 0 ){
    if( pEx->report )
      REPORT("",0,EX_ERROR_LABEL_NOT_DEFINED,LabelName);
    else
      pEx->iErrorCounter++;
    }
  }

/*POD
=H ex_CheckUndefinedLabels()

This function traverses the label symbol table and reports all undefined
labels as error. Undefined labels reference the node with node-number zero. Jumping
on a label like that caused the program to stop instead of compile time error
in previous versions.

/*FUNCTION*/
void ex_CheckUndefinedLabels(peXobject pEx
  ){
/*noverbatim
CUT*/
  sym_TraverseSymbolTable(pEx->GlobalLabels,CUL_callback,pEx);
  }

/*POD
=H ex_CleanNameSpaceStack()

This function cleans the name space stack. This cleaning does not need to be done during
syntax analysis. It is needed after the analysis has been done to detect unclosed modules.

Note that the T<main::> module is implicit and can not and should not be closed
unless it was explicitly opened.

The function calls the report function if the name space is not empty when the function is called.
/*FUNCTION*/
void ex_CleanNameSpaceStack(peXobject pEx
  ){
/*noverbatim
CUT*/
   pNameSpaceStack p;

  if( pEx->pOldNameSpace )
    REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNFINISHED_MODULE,NULL);
  while( pEx->pOldNameSpace ){
    p = pEx->pOldNameSpace;
    pEx->pOldNameSpace = pEx->pOldNameSpace->next;
    alloc_Free(p->ThisNameSpace,pEx->pMemorySegment);
    alloc_Free(p,pEx->pMemorySegment);
    }
  }

/*POD
=H expression_PopNameSpace()

When a T<module name> instruction is encountered the name space is modified. However
the old name space should be reset when an T<end module> statement is reached. As the
modules can be nested into each other the name spaces are stored in a name space stack
during syntax analysis.

This function pops the name space from the name space stack and copies the value to the
T<pEx->>T<CurrentNameSpace> variable. This should be executed when a name space is closed
and we want to return to the embedding name space.

/*FUNCTION*/
int expression_PopNameSpace(peXobject pEx
  ){
/*noverbatim
CUT*/
  pNameSpaceStack p;

  if( (p=pEx->pOldNameSpace) == NULL )return EX_ERROR_NO_OLD_NAMESPACE;

  pEx->pOldNameSpace = pEx->pOldNameSpace->next;
  strcpy(pEx->CurrentNameSpace,p->ThisNameSpace);

  alloc_Free(p->ThisNameSpace,pEx->pMemorySegment);
  alloc_Free(p,pEx->pMemorySegment);
  return EX_ERROR_SUCCESS;
  }

/*POD
=H ex_PushWaitingLabel()

This function is used to define a label.

/*FUNCTION*/
int ex_PushWaitingLabel(peXobject pEx,
                         pSymbolLABEL pLbl
  ){
/*noverbatim

When a label is defined the T<eNode_l> that the label is going to belong still does not exists, and
therefore the T<NodeId> of that T<eNode_l> is not known. This function together with R<ex_PopWaitingLabel()>
maintains a stack that can store labels which are currently defined and still need a line to be assigned
to them. These labels all point to the same line. Altough it is very rare that many labels point to
the same line, it is possible. The number of labels that can point the same line is defined by the
constant T<MAX_SAME_LABELS> defined in T<expression.c>

To make it clear see the following BASIC code:

=verbatim

this_is_a_label:
REM this is a comment
            PRINT "hello word!!"

=noverbatim

The label is defined on the first line of the example. However the label belongs to the
third line containing the statement T<PRINT>. When the label is processed the compiler does
not know the node number of the code segment which is generated from the third line. Therefore
this function maintains a label-stack to store all labels that need a line. Whenever a line is
compiled so that a label can be assigned to that very line the stack is emptied and all labels waiting on the
stack are assigned to the line just built up. (Or the line is assigned to the labels if you
like the sentence the other way around.)

Note that not only labels given by a label defining statement are pushed on this stack, but also
labels generated by commands like 'while/wend' of 'if/else/endif'.

CUT*/
  if( pEx->cLabelsWaiting < MAX_SAME_LABELS ){
    pEx->LabelsWaiting[pEx->cLabelsWaiting++] = pLbl;
    return EX_ERROR_SUCCESS;
    }
  return EX_ERROR_TOO_MANY_WAITING_LABEL;
  }

/*POD
=H ex_PopWaitingLabel()

This function is used to get a label out of the waiting-label-stack.

/*FUNCTION*/
pSymbolLABEL ex_PopWaitingLabel(peXobject pEx
  ){
/*noverbatim

To get some description of waiting labels see the description of the function R<ex_PushWaitingLabel()>.

CUT*/
  if( pEx->cLabelsWaiting == 0 )return NULL;
  return pEx->LabelsWaiting[ -- (pEx->cLabelsWaiting) ];
  }

/*POD
=H _ex_PushLabel()

This function is used to push an unnamed label on the compile time stack.
For more detailed defintion of the unnamed labels and this stack see the
documentation of the function R<ex_PopLabel()>.

/*FUNCTION*/
int _ex_PushLabel(peXobject pEx,
                  pSymbolLABEL pLbl,
                  long Type,
                  void *pMemorySegment
  ){
/*noverbatim

The argument T<Type> is used to define the type of the unnamed label. This is usually defined
in the table created by the program T<syntaxer.pl>

=bold
Do NOT get confused! This stack is NOT the same as the waiting label stack. That is usually for named
labels.
=nobold

However the non-named labels are also pushed on that stack before they get value.

CUT*/
  pLabelStack p;

  if( pEx->pFreeComeAndGoStack == NULL ){
    pEx->pFreeComeAndGoStack = alloc_Alloc(sizeof(LabelStack),pMemorySegment);
    if( pEx->pFreeComeAndGoStack == NULL )return EX_ERROR_MEMORY_LOW;
    pEx->pFreeComeAndGoStack->Flink = NULL;
    }
  p = pEx->pFreeComeAndGoStack;
  pEx->pFreeComeAndGoStack = pEx->pFreeComeAndGoStack->Flink;
  p->Flink = pEx->pComeAndGoStack;
  p->Type = Type;
  pEx->pComeAndGoStack = p;
  p->pLabel = pLbl;  
  return 0;
  }

/*POD
=H _ex_PopLabel()

This function is used to pop an unnamed label off the compile stack.

When a construct, like T<IF/ELSE/ENDIF> or T<REPEAT/UNTIL> or T<WHILE/WEND> is created
it is defined using compile time label stack.

For example analyzing the instruction T<WHILE> pushes a "go forward" value on the compile time
label stack. When the instruction T<WEND> is analyzed it pops off the value and stores
T<NodeId> for the label. The label itself is not present in the global label symbol table,
because it is an unnamed label and is referenced during compile time by the pointer to the
label structure.

The value of the T<AcceptedType> ensures that a T<WEND> for example do not matches an T<IF>.


/*FUNCTION*/
pSymbolLABEL _ex_PopLabel(peXobject pEx,
                          long *pAcceptedType
  ){
/*noverbatim

The array T<pAcceptedType> is an array of long values that have T<MAX_GO_CONSTANTS> values.
This is usually points to a static table element which is generated by the program T<syntaxer.pl>.

=bold
Do NOT get confused! This stack is NOT the same as the waiting label stack. That is for named
labels.
=nobold
CUT*/
  pLabelStack q;
  pSymbolLABEL p;
  int i = MAX_GO_CONSTANTS;
  long lTypeOnStack;

  if( pEx->pComeAndGoStack == NULL )return NULL;
  p = pEx->pComeAndGoStack->pLabel;
  lTypeOnStack = pEx->pComeAndGoStack->Type;
  pEx->pComeAndGoStack->pLabel = NULL; /* be safe */
  q = pEx->pComeAndGoStack->Flink;
  pEx->pComeAndGoStack->Flink = pEx->pFreeComeAndGoStack;
  pEx->pFreeComeAndGoStack = pEx->pComeAndGoStack;
  pEx->pComeAndGoStack = q;

  if( pAcceptedType ){ /* passing NULL means that we do not care the type (usually to clean up the stack) */
    while( i-- )
      if( *pAcceptedType++ == lTypeOnStack )return p;
    REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_BAD_NESTING,NULL);
    }

  return p;
  }

/*POD
=H _ex_CleanLabelStack()

This function is used to clean the unnamed label stack whenever
a locality is left. This helps to detect when an instruction like
T<FOR> or T<WHILE> is not closed within a function.
/*FUNCTION*/
void _ex_CleanLabelStack(peXobject pEx
  ){
/*noverbatim
CUT*/

  if( ex_PopLabel(NULL) )
    REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNFINISHED_NESTING,NULL);
  while( ex_PopLabel(NULL) );
  }

/*POD
=H Some NOTE on SymbolXXX functions

The functions named T<SymbolXXX> like T<SymbolLABEL>, or T<SymbolUF> do NOT store 
the names of the symbols. They are named T<SymbolXXX> because they are natural
extensions of the symbol table system. In other compilers the functionality to
retrieve the arguments of a symbol is part of the symbol table handling routines.

In script basic the symbol table handling routines were developed to be general purpose.
Therefore all the arguments the symbol table functions bind toa symbol is a T<void *>
pointer. This pointer points to a struct that holds the arguments of the symbols,
and the functions T<SymbolXXX> allocate the storage for the arguments.

This way it is possible to allocate arguments for non-existing symbols, as it is done
for labels. Script basic uses non-named labels to arrange the "jump" instructions for
T<IF/ELSE/ENDIF> constructs. (And for some other constructs as well.) The label and
jump constructs look like:

=verbatim

       IF expression Then

       ELSE
label1:


       END IF
label2:

=noverbatim

The labels T<label1> and T<label2> do not have names in the system, not even autogenerated names.
They are referenced via pointers and their value (the T<NodeId> of the instruction) get into the
T<SymbolLABEL> structure and later int o the T<cNODE> during build.

CUT*/

/*POD
=H _new_SymbolLABEL()

This function should be used to create a new label. The label can be named or unnamed. Note that
this structure does NOT contain the name of the label.

/*FUNCTION*/
pSymbolLABEL _new_SymbolLABEL(peXobject pEx
  ){
/*noverbatim

Also note that all labels are global in a basic program and are subject to name space decoration.
However the same named label can not be used in two different functions in the same name space.

A label has a serial value, which is not actually used and a number of the node that it points to.

See the comments on R<ex_symbols()>.
CUT*/
  pSymbolLABEL p;

  p = (pSymbolLABEL)alloc_Alloc(sizeof(SymbolLABEL),pEx->pMemorySegment);
  if( p == NULL )return NULL;
  pEx->cGlobalLabels ++;
  p->Serial = pEx->cGlobalLabels;
  p->node = 0; /* this means that the struct has no value */
  return p;
  }

/*POD
=H _new_SymbolVAR()

This function should be used to create a new variable during compile time. A
variable is nothing else than a serial number. This serial number starts
from 1.

/*FUNCTION*/
pSymbolVAR _new_SymbolVAR(peXobject pEx,
                          int iLocal
  ){
/*noverbatim

The second argument should be true for local variables. The counting of local
variables are reset whenever the program enters a new locality. Localities can
not be nested.

Also note that local variables are allocated in a different segment because they
are deallocated whenever the syntax analyzer leaves a locality.
CUT*/
  pSymbolVAR p;

  if( iLocal ){
    p = (pSymbolVAR)alloc_Alloc(sizeof(SymbolVAR),pEx->pLocalVarMemorySegment);
    if( p == NULL )return NULL;
    pEx->cLocalVariables++;
    p->Serial = pEx->cLocalVariables;
    }else{
    p = (pSymbolVAR)alloc_Alloc(sizeof(SymbolVAR),pEx->pMemorySegment);
    if( p == NULL )return NULL;
    pEx->cGlobalVariables++;
    p->Serial = pEx->cGlobalVariables;
    }

  return p;
  }

/*POD
=H _new_SymbolUF()

This function should be used to create a new user defined function symbol.
/*FUNCTION*/
pSymbolUF _new_SymbolUF(peXobject pEx
  ){
/*noverbatim

A user function is defined by its serial number (serial number is actually not used in the
current sytsem) and by the node number where the function actually starts.

The number of arguments and the number of local variables are defined in the generated
command and not in the symbol table. This way these numbers are available as they should be
during run time.
CUT*/
  pSymbolUF p;

  p = (pSymbolUF)alloc_Alloc(sizeof(SymbolUF),pEx->pMemorySegment);
  if( p == NULL )return NULL;

  pEx->cUserFunctions ++;
  p->FunId = pEx->cUserFunctions;
  p->next = pEx->FirstUF;
  pEx->FirstUF = p;
  p->node = 0L;

  return p;
  }

/*POD
=H _new_eNODE()

This function should be used to create a new T<eNODE>.

/*FUNCTION*/
peNODE _new_eNODE(peXobject pEx
  ){
/*noverbatim

Each T<eNODE> and T<eNODE_l> structure has a serial number. The T<eNODE>s
are referencing each other using pointers. However after build these pointers
become integer numbers that refer to the ordinal number of the node. Nodes are
stored in a single memory block after they are packed during build.

An T<eNODE> is a structure that stores a unit of compiled code. For example
an addition in an expression is stored in an T<eNODE> containing the code for the
addition operator and containing pointers to the operands.

CUT*/
  peNODE p;

  p = (peNODE)alloc_Alloc(sizeof(eNODE),pEx->pMemorySegment);
  if( p == NULL )return NULL;
  pEx->NodeCounter ++;
  p->NodeId = pEx->NodeCounter;
  if( pEx->pLex->pLexCurrentLexeme ){
    p->szFileName = pEx->pLex->pLexCurrentLexeme->szFileName;
    p->lLineNumber = pEx->pLex->pLexCurrentLexeme->lLineNumber;
    }else{
    p->szFileName = NULL;
    p->lLineNumber = 0;
    }

  return p;
  }
/*POD
=H _new_eNODE_l()

This function should be used to create a new T<eNODE> list. This is nothing else
than a simple structure having two pointers. One pointer points to an T<eNODE>
while the other points to the next T<eNODE_l> struct or to NULL if the current
T<eNODE_l> is the last of a list.

/*FUNCTION*/
peNODE_l _new_eNODE_l(peXobject pEx,
                      char *pszFileName,
                      long lLineNumber
  ){
/*noverbatim

Note that T<eNODE> and T<eNODE_l> are converted to the same type of 
structure during build after the syntactical analysis is done.
CUT*/
  peNODE_l p;

  p = ((peNODE_l)alloc_Alloc(sizeof(eNODE_l),pEx->pMemorySegment));
  if( p == NULL )return NULL;
  pEx->NodeCounter ++;
  p->NodeId = pEx->NodeCounter;
  p->szFileName = NULL;
  p->lLineNumber = 0;
  if( pszFileName ){
    p->szFileName = pszFileName;
    p->lLineNumber = lLineNumber;
    }else
  if( pEx->pLex && pEx->pLex->pLexCurrentLexeme ){
    p->szFileName = pEx->pLex->pLexCurrentLexeme->szFileName;
    p->lLineNumber = pEx->pLex->pLexCurrentLexeme->lLineNumber;
    }
  /* initializing these pointers here makes life safer */
  p->rest = NULL;
  p->actualm = NULL;

  return p;
  }

/*POD
=H ex_free()

This function releases all memory that was allocated during syntax analysis.

/*FUNCTION*/
void ex_free(peXobject pEx
  ){
/*noverbatim
CUT*/

  alloc_FinishSegment(pEx->pMemorySegment);
  alloc_FinishSegment(pEx->pLocalVarMemorySegment);
  alloc_FinishSegment(pEx->pSymbolTableMemorySegment);
  pEx->pMemorySegment = NULL;
  }

/*POD
=H ex_init()

This function should be called before starting syntactical analysis. This
function 
=itemize
=item positions the lexeme pointer to the first lexeme,
=item initializes the memory segments needed for structured memory allocation, 
=item created the symbol tables
=item initializes 'class' variables
=item initializes the name space to be T<main::>
=noitemize

/*FUNCTION*/
int ex_init(peXobject pEx
  ){
/*noverbatim
CUT*/
  long i;


  lex_StartIteration(pEx->pLex);

  pEx->pMemorySegment
                      = alloc_InitSegment(pEx->memory_allocating_function,
                                          pEx->memory_releasing_function);
  if( pEx->pMemorySegment == NULL )return EX_ERROR_MEMORY_LOW;

  pEx->pSymbolTableMemorySegment
                      = alloc_InitSegment(pEx->memory_allocating_function,
                                          pEx->memory_releasing_function);
  if( pEx->pSymbolTableMemorySegment == NULL )return EX_ERROR_MEMORY_LOW;

  pEx->pLocalVarMemorySegment = alloc_InitSegment(pEx->memory_allocating_function,
                                          pEx->memory_releasing_function);
  if( pEx->pLocalVarMemorySegment == NULL )return EX_ERROR_MEMORY_LOW;


  pEx->GlobalVariables  = sym_NewSymbolTable(alloc_Alloc,pEx->pSymbolTableMemorySegment);
  pEx->GlobalLabels     = sym_NewSymbolTable(alloc_Alloc,pEx->pSymbolTableMemorySegment);
  pEx->GlobalConstants  = sym_NewSymbolTable(alloc_Alloc,pEx->pSymbolTableMemorySegment);

  if( pEx->GlobalVariables == NULL ||
      pEx->GlobalLabels == NULL ||
      pEx->GlobalConstants == NULL )return EX_ERROR_MEMORY_LOW;

  pEx->LocalVariables   = NULL; /* it is initialized when we go local */
  pEx->LocallyDeclaredGlobalVariables = NULL; /* it is initialized when we go local */
  pEx->UserFunctions    = sym_NewSymbolTable(alloc_Alloc,pEx->pSymbolTableMemorySegment);
  if( pEx->UserFunctions == NULL )return EX_ERROR_MEMORY_LOW;

  pEx->ThisFunction = NULL;

  pEx->NodeCounter = 0;

  /* no function is defined currently */
  pEx->pFunctionWaiting = NULL;

  /* no user functions are defined */
  pEx->FirstUF = NULL;

  /* there are no waiting come backs, nor waitiong go forwards */
  pEx->pComeAndGoStack = NULL;
  pEx->pFreeComeAndGoStack = NULL;

  /* we start with global variables and go to local when entering a user defined function */
  pEx->iWeAreLocal = 0;
  /* the default language feature is that globals need not be declared */
  pEx->iDeclareVars = 0;
  /* the default language feature is that undeclared variables are global */
  pEx->iDefaultLocal = 0;

  pEx->Buffer   = alloc_Alloc(pEx->cbBuffer*sizeof(char),pEx->pMemorySegment);
  pEx->CurrentNameSpace = alloc_Alloc(pEx->cbCurrentNameSpace*sizeof(char),pEx->pMemorySegment);

  if( !pEx->GlobalVariables  || 
      !pEx->UserFunctions    ||
      !pEx->BuiltInFunctions ||
      !pEx->GlobalLabels     ||
      !pEx->GlobalConstants  ||
      !pEx->Binaries         ||
      !pEx->CurrentNameSpace ||
    0
    ){
    ex_free(pEx);
    return EX_ERROR_MEMORY_LOW;
    }

  pEx->cGlobalLabels = 0;
  pEx->cGlobalVariables = 0;
  /* pEx->cLocalVariables  = 0; /* this is initialized when we go local */
  pEx->cUserFunctions   = 0;

  if( pEx->cbCurrentNameSpace < 7 ){
    ex_free(pEx);
    return EX_ERROR_TOO_LONG_NAME_SPACE;
    }
  strcpy(pEx->CurrentNameSpace,"main::");
  pEx->pOldNameSpace = NULL;
  pEx->cbStringTable = 0L;

  for( i=0 ; pEx->PredeclaredLongConstants[i].name ; i++ )
    ex_PredeclareGlobalLongConst(pEx,
                                 pEx->PredeclaredLongConstants[i].name,
                                 pEx->PredeclaredLongConstants[i].value);

  return EX_ERROR_SUCCESS;
}

/*POD
=H ex_CleanNamePath()

This function created a normalized name space name from a non normalized. This is a simple
string operation.

Think of name space as directories and variables as files. A simple variable name is in the
current name space. If there is a 'path' before the variable or function name the path has to be
used. This path can either be relative or absolute.

File system:

T< ../ > is used to denote the parent directory in file systems.

Name space:

T< _::> is used to denote the parent name space.

File system:

T< mydir/../yourdir> is the same as T<yourdir>

Name space:

T< myns::_::yourns> is the same as T<yourns>

This function removes the unneccesary downs and ups from the name space and creates the
result in the same buffer as the original. This can always be done as the result is always 
shorter. (Well, not longer.)

/*FUNCTION*/
void ex_CleanNamePath(char *s
  ){
/*noverbatim
CUT*/
  int i,j;
  int f; /* flag if we have found something to remove */

  while(1){
    j = 0; f = 0;
    for( i=0 ; s[i] ; i++ ){
      if( s[i] == ':' && s[i+1] == ':' && s[i+2] == '_' && s[i+3] == ':' && s[i+4] == ':' ){
        f = 1; /* relative upreference, like package::_::v is found */
        i += 5;
        break;
        }
      if( s[i] == ':' && s[i+1] == ':' ){
        j = i+2;
        i ++;
        continue;
        }
      }
    if( !f )return;
    while( s[j]=s[i] )i++,j++;/* pull down the end, and ...*/
    /* start over */
    }
  }

/*POD
=H ex_ConvertName()

Use this function to convert a relative name to absolute containing name space.

This function checks if the variable or function name is relative or absolute. If the
name is relative it creates the absolute name using the current name space as a base.

The result is always put into the T<Buffer>.

A name is relative if it does NOT contain T<::> at all (implicit relative),
if it starts with T<::> or is it starts with T<_::> (explicit relative).

/*FUNCTION*/
int ex_ConvertName(char *s,          /* name to convert            */
                   char *Buffer,     /* buffer to store the result */
                   size_t cbBuffer,  /* size of the buffer         */
                   peXobject pEx     /* current expression object  */
  ){
/*noverbatim

The error value is T<EX_ERROR_SUCCESS> (zero) menaing succesful conversion or
T<EX_ERROR_TOO_LONG_VARIABLE> meaning that the variable is too long for the
buffer.

Note that the buffer is allocated in R<ex_init()> according to the size value given in
the class variable T<cbBuffer>, which should be set by the main function calling
syntax analysis.
CUT*/
  int i;

  /* This is a zero or one character variable,
     it can not contain :: and therefore
     it is relative variable.                  */
  if( !s[0] || !s[1] ){
    if( cbBuffer < strlen(pEx->CurrentNameSpace) + strlen(s) + 1 )
      return EX_ERROR_TOO_LONG_VARIABLE;
    strcpy(Buffer,pEx->CurrentNameSpace);
    strcat(Buffer,s);
    return EX_ERROR_SUCCESS;
    }

  /* This starts with :: like ::variable or ::subpackage::variable
     This is an explicit relative variable.  */
  if( s[0] == ':' && s[1] == ':' ){
    if( cbBuffer < strlen(pEx->CurrentNameSpace) + strlen(s) - 1 )
      return EX_ERROR_TOO_LONG_VARIABLE;
    strcpy(Buffer,pEx->CurrentNameSpace);
    strcat(Buffer,s+2); /* current_name_space contains the trailing ::
                           we should not copy it twice. */
    ex_CleanNamePath(Buffer);    /* remove the package::_ references */
    return EX_ERROR_SUCCESS;
    }

  /* This starts with _:: like _::variable or _::subpackage::variable
     This is an explicit relative variable.  */
  if( s[0] == '_' && s[1] == ':' && s[2] == ':' ){
    if( cbBuffer < strlen(pEx->CurrentNameSpace) + strlen(s) + 1 )
      return EX_ERROR_TOO_LONG_VARIABLE;
    strcpy(Buffer,pEx->CurrentNameSpace);
    strcat(Buffer,s);
    ex_CleanNamePath(Buffer);    /* remove the package::_ references */
    return EX_ERROR_SUCCESS;
    }

  /* This is long enough to contain ::, and does not start with ::  */
  for( i=1 ; s[i] ; i++ ){
    /* if it contains :: inside and not in front then it is an absolute
       reference. */
    if( s[i] == ':' && s[i+1] == ':' ){
      if( cbBuffer < strlen(pEx->CurrentNameSpace) + 1 )
        return EX_ERROR_TOO_LONG_VARIABLE;
      strcpy(Buffer,s);
      ex_CleanNamePath(Buffer); /* let the user to write dirty paths if she wishes*/
      return EX_ERROR_SUCCESS;
      }
    }

   /* Finally this is a simple implicit relative variable
      without any :: inside or in front. */
   if( cbBuffer < strlen(pEx->CurrentNameSpace) + strlen(s) + 1 )
     return EX_ERROR_TOO_LONG_VARIABLE;
   strcpy(Buffer,pEx->CurrentNameSpace);
   strcat(Buffer,s);
   return EX_ERROR_SUCCESS;
}

/*POD
=H ex_IsBFun()

This function checks if the current lexeme is a built-in function and
returns pointer to the function in the table T<BuiltInFunctions> or
returns NULL if the symbol is not a built-in function.

/*FUNCTION*/
pBFun ex_IsBFun(peXobject pEx
  ){
/*noverbatim
CUT*/
  pBFun p;

  if( LexemeType != LEX_T_NSYMBOL )return NULL;
  p = pEx->BuiltInFunctions;

  while( p->OpCode && (long)p->OpCode != LexemeCode )p++;
  if( p->OpCode )return p;
  return NULL;
  }

/*POD
=H ex_IsUnop()

This function checks if the current lexeme is an unary operator and
returns the op code or zero if the lexem is not an unary operator.

/*FUNCTION*/
unsigned long ex_IsUnop(peXobject pEx
  ){
/*noverbatim
CUT*/
  unsigned long *p;

  if( LexemeType != LEX_T_NSYMBOL && LexemeType != LEX_T_ASYMBOL )return 0;
  p = pEx->Unaries;

  while( *p && (long)*p != LexemeCode )p++;
  return *p;
  }

/*POD
=H ex_IsBinop()

This function checks if the current lexeme is a binary operator of the given precedence
and returns the op code or zero.

/*FUNCTION*/
unsigned long ex_IsBinop(peXobject pEx,
               unsigned long precedence
  ){
/*noverbatim
CUT*/
  unsigned long *p;

  if( LexemeType != LEX_T_NSYMBOL && LexemeType != LEX_T_ASYMBOL )return 0;
  p = pEx->Binaries;

  while( *p && *p != (unsigned)LexemeCode )p += 2;
  if( *p && p[1] == precedence )return *p;
  return 0;
  }


/*POD
=H ex_LeftValueList()

This function works up a T<leftvalue_list> pseudo terminal and creates the nodes for it.

/*FUNCTION*/
peNODE_l ex_LeftValueList(peXobject pEx
  ){
/*noverbatim
CUT*/
  peNODE_l r;
  peNODE   q;

  q = ex_LeftValue(pEx);
  if( ! q )return NULL;
  r = new_eNODE_l();
  r->actualm = q;
  r->rest = NULL;
  if( LexemeType == LEX_T_CHARACTER && LexemeChar == ',' ){
    NextLexeme;
    r->rest = ex_LeftValueList(pEx);
    }
  return r;  
  }

/*POD
=H ex_ExpressionList()

This function works up an T<expression_list> pseudo terminal and creates
the nodes for it.
/*FUNCTION*/
peNODE_l ex_ExpressionList(peXobject pEx
  ){
/*noverbatim
CUT*/
  peNODE_l r;
  peNODE   q;

  q = ex_Expression_i(pEx,pEx->MAXPREC);
  if( ! q )return NULL;
  r = new_eNODE_l();
  r->actualm = q;
  r->rest = NULL;
  if( LexemeType == LEX_T_CHARACTER && LexemeChar == ',' ){
    NextLexeme;
    r->rest = ex_ExpressionList(pEx);
    if( r->rest == NULL )return NULL;
    }
  return r;  
  }

/*POD
=H ex_Local()

This function work up a T<local> pseudo terminal. This does not create any node.

/*FUNCTION*/
int ex_Local(peXobject pEx
  ){
/*noverbatim
The return value is T<0> if no error happens.

T<1> means sytax error (the coming token is not a symbol)

T<2> means that there is no local environment (aka. the T<local var> is not inside a function)
CUT*/
  void **pSymbol;

  if( LexemeType != LEX_T_ASYMBOL )return 1;
  ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
  if( WeAreNotLocal )return 2;
  pSymbol = sym_LookupSymbol(pEx->Buffer,        /* symbol we search */
                             pEx->LocallyDeclaredGlobalVariables,/* in this table */
                             0,                  /* do not insert the symbol as new */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pLocalVarMemorySegment);
  if( pSymbol )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_GLODEF,pEx->Buffer);
  pSymbol = sym_LookupSymbol(pEx->Buffer,        /* symbol we search */
                             pEx->LocalVariables,/* in this table */
                             1,                  /* insert the symbol as new */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pLocalVarMemorySegment);
  /* if this variable was not declared yet as local then allocate a serial
     number for it and place to store the compile time information for it */
  if( *pSymbol == NULL )
    *pSymbol = (void *)new_SymbolVAR(LOCAL_VAR);
  NextLexeme;
  return 0;
  }

/*POD
=H ex_LocalList()

This function work up a T<local_list> pseudo terminal. This does not generate any node.

/*FUNCTION*/
int ex_LocalList(peXobject pEx
  ){
/*noverbatim
The return value is T<0> if no error happens.

T<1> means sytax error (the coming token is not a symbol)

T<2> means that there is no local environment (aka. the T<local var> is not inside a function)
CUT*/
  int iErr;

  iErr = ex_Local(pEx);
  if( iErr )return iErr;
  while( LexemeType == LEX_T_CHARACTER && LexemeChar == ',' ){
    NextLexeme;
    iErr = ex_Local(pEx);
    if( iErr )return iErr;
    }
  return 0;
  }




/*POD
=H ex_Global()

This function work up a T<global> pseudo terminal. This does not create any node.

/*FUNCTION*/
int ex_Global(peXobject pEx
  ){
/*noverbatim
The return value is T<0> if no error happens or the error is semantic and was
reported (global variable redefinition).

T<1> means syntax error (the coming token is not a symbol)

CUT*/
  void **pSymbol;
  void **plSymbol;

  if( LexemeType != LEX_T_ASYMBOL )return 1;
  ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);

  /* when we are inside a sub or function (when we are local)
     GLOBAL only means that we want to use this global variable
     but it has to be declared before in global scope
   */
  if( WeAreLocal ){
    pSymbol = sym_LookupSymbol(pEx->Buffer,         /* symbol we search */
                              pEx->GlobalVariables,/* in this table */
                              0,
                              alloc_Alloc,
                              alloc_Free,
                              pEx->pMemorySegment);
    if( pSymbol == NULL ){
      if( DeclareVars )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
      /* ok error was reported (or not) now declare the variable not to propagate the error */
      pSymbol = sym_LookupSymbol(pEx->Buffer,         /* symbol we search */
                                pEx->GlobalVariables,/* in this table */
                                1,                   /* insert the symbol as new */
                                alloc_Alloc,
                                alloc_Free,
                                pEx->pMemorySegment);
      }
    plSymbol = sym_LookupSymbol(pEx->Buffer,
                                pEx->LocalVariables,/* in this table */
                                0,                   /* do not insert the symbol as new */
                                alloc_Alloc,
                                alloc_Free,
                                pEx->pLocalVarMemorySegment);
    if( plSymbol ){/* this is declared as global, but it was already declared as local, confusing */
      REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_GLODEF,pEx->Buffer);
      NextLexeme;
      return 0;
      }
    /* now insert the symboil into this table so that we know that this global variable is in use in this subroutine */
    sym_LookupSymbol(pEx->Buffer,         /* symbol we search */
                      pEx->LocallyDeclaredGlobalVariables,/* in this table */
                      1,                   /* insert the symbol as new */
                      alloc_Alloc,
                      alloc_Free,
                      pEx->pLocalVarMemorySegment);
    }else{/* if WeAreNotLocal */
    pSymbol = sym_LookupSymbol(pEx->Buffer,        /* symbol we search         */
                              pEx->GlobalVariables,/* in this table            */
                              1,                   /* insert the symbol as new */
                              alloc_Alloc,
                              alloc_Free,
                              pEx->pMemorySegment);
    if( *pSymbol ){
      REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_GREDEF,pEx->Buffer);
      NextLexeme;
      return 0;
      }
    }
  *pSymbol = (void *)new_SymbolVAR(GLOBAL_VAR);
  NextLexeme;
  return 0;
  }

/*POD
=H ex_GlobalList()

This function work up a T<global_list> pseudo terminal. This does not generate any node.

/*FUNCTION*/
int ex_GlobalList(peXobject pEx
  ){
/*noverbatim
The return value is T<0> if no error happens.

T<1> means sytax error (the coming token is not a symbol)

T<2> means the variable was already defined
CUT*/
  int iErr;

  iErr = ex_Global(pEx);
  if( iErr )return iErr;
  while( LexemeType == LEX_T_CHARACTER && LexemeChar == ',' ){
    NextLexeme;
    iErr = ex_Global(pEx);
    if( iErr )return iErr;
    }
  return 0;
  }


/*POD
=H ex_LookupUserFunction()

This function searches a user defined function and returns a pointer to the symbol table entry.
If the second argument T<iInsert> is true the symbol is inserted into the table and an
undefined function is created. This is the case when a function is used before declared. If the
argument T<iInsert> is fales T<NULL> is returned if the function is not yet defined.

/*FUNCTION*/
void **ex_LookupUserFunction(peXobject pEx,
                             int iInsert
  ){
/*noverbatim
CUT*/
  void **pSymbol;

  pSymbol = sym_LookupSymbol(pEx->Buffer,
                             pEx->UserFunctions,
                             iInsert,
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pSymbolTableMemorySegment);

  return pSymbol;
  }

/*POD
=H ex_LookupGlobalVariable

This function searches the global variable symbol table to find the global variable
with the name stored in T{pEx->Buffer}. If the variable was not declared then this function
inserts the variable into the symbol table if the argument T<iInsert> is true,
but nothing more: the symbol table entry remains T<NULL>.

/*FUNCTION*/
void **ex_LookupGlobalVariable(peXobject pEx,
                               int iInsert
  ){
/*noverbatim
The function returns pointer to the pointer stored in the symbol table associated with the global
variable.
CUT*/
  void **pSymbol;

  pSymbol = sym_LookupSymbol(pEx->Buffer,        /* the symbol we search */
                             pEx->GlobalVariables,/* in the global table */
                             iInsert,                   /* insert automatically as new if not found */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pSymbolTableMemorySegment);
  return pSymbol;
  }

/*POD
=H ex_LookupLocallyDeclaredGlobalVariable

This function searches the global variable symbol table to find the global variable
with the name stored in T{pEx->Buffer}. If the variable was not declared then this function
return T<NULL>. Othervise it returns a pointer to a T<void *> pointer, which is 
T<NULL>.

Note that this table is allocated when the program starts a T<sub> or T<function> (aka. when
we go local) and is used to register, which variables did the program declare
as global variables inside the subroutine. There is no any value associated with the symbols
in this table, as the symbols are also inserted into the global symbol table which serves
the purpose.

/*FUNCTION*/
void **ex_LookupLocallyDeclaredGlobalVariable(peXobject pEx
  ){
/*noverbatim
The function returns pointer to the pointer stored in the symbol table associated with the global
variable or T<NULL>.
CUT*/
  void **pSymbol;

  pSymbol = sym_LookupSymbol(pEx->Buffer,        /* the symbol we search */
                             pEx->LocallyDeclaredGlobalVariables,/* in the global table */
                             0,                   /* do not insert!  */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pLocalVarMemorySegment);
  return pSymbol;
  }

/*POD
=H ex_LookupLocalVariable

This function searches the local variable symbol table to find the local variable
with the name stored in T{pEx->Buffer}. If the variable was not declared and the argument T<iInsert>
is true then then this function inserts the variable into the symbol table, 
but nothing more: the symbol table entry remains
T<NULL>.

/*FUNCTION*/
void **ex_LookupLocalVariable(peXobject pEx,
                              int iInsert
  ){
/*noverbatim
The function returns pointer to the pointer stored in the symbol table associated with the global
variable.
CUT*/
  void **pSymbol;

  pSymbol = sym_LookupSymbol(pEx->Buffer,        /* the symbol we search */
                             pEx->LocalVariables,/* in the actual local table */
                             iInsert,                   /* insert automatically as new if not found */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pLocalVarMemorySegment);
  return pSymbol;
  }

/*POD
=H ex_Tag

This function implements the syntax analysis for the lowest syntax elements of an expression.
This function is called when syntax analysis believes that a TAG has to be worked up
in an expression. A tag is defined formally as

=verbatim
 tag ::= UNOP tag
         BUN '(' expression_list ')'
         NUMBER
         STRING
         '(' expression ')'
         VARIABLE { '[' expression_list ']' }
         VARIABLE '{' expression_list '}'
         FUNC '(' expression_list ')'
        .
=noverbatim

/*FUNCTION*/
peNODE ex_Tag(peXobject pEx
  ){
/*noverbatim
The function returns pointer to the new node.
CUT*/

/*
Hey!!! Unary operators do not have precedence! They are above all! Is this
your original intention, or did it just came out like this?!!
*/
  peNODE q,r;
  peNODE_l z;
  long OpCode;
  char *s,*pszFN;
  int is_local;
  int is_assoc; /* the array reference we are currently analize is associative */
  long arg_count;
  void **pSymbol;
  pBFun pFunction;
  pLexeme pConstantLexeme;

  /* BUN '(' expression_list ')' */
  if( pFunction = ex_IsBFun(pEx) ){
    q = new_eNODE();
    if( q == NULL )return NULL;
    q->OpCode = LexemeCode;
    NextLexeme;
    if( LexemeType == LEX_T_CHARACTER && LexemeChar == '('  ){
      NextLexeme;
      if( LexemeType == LEX_T_CHARACTER && LexemeChar == ')' ){
        NextLexeme;
        goto no_arguments; /* Sorry for the construct, I know this is dirty. */
        }
      q->Parameter.Arguments.Argument = ex_ExpressionList(pEx);
      if( LexemeType != LEX_T_CHARACTER || LexemeChar != ')' ){/* the closing ) is missing after function call */
        REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_PAREN,NULL);
        }else{ NextLexeme; }
      /* go and check the number of arguments */
      z = q->Parameter.Arguments.Argument;
      arg_count = 0;
      while( z ){
        z = z->rest;
        arg_count++;
        }
      if( arg_count < pFunction->MinArgs )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_TOO_FEW_ARGUMENTS,NULL);
      if( arg_count > pFunction->MaxArgs )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_TOO_MANY_ARGUMENTS,NULL);
      return q;
      }else{/* if there is no '(' after the name of the built in function */
no_arguments:
      q->Parameter.Arguments.Argument = NULL;
      /* having a function call w/o () is OK if there is no need for arguments */
      if( pFunction->MinArgs == 0 )return q;
      REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_FUNCTION_NEEDS_ARGUMENTS,NULL);
      return q;
      }
    }

  /* UNOP tag */
  if( OpCode = ex_IsUnop(pEx) ){
    q = new_eNODE();
    if( q == NULL )return NULL;
    q->OpCode = OpCode;
    NextLexeme;
    q->Parameter.Arguments.Argument = new_eNODE_l();
    if( q->Parameter.Arguments.Argument == NULL ){
      alloc_Free(q,pEx->pMemorySegment);
      return NULL;
      }
    q->Parameter.Arguments.Argument->actualm = ex_Tag(pEx);
    q->Parameter.Arguments.Argument->rest = NULL;
    return q;
    }

  /* '(' expression ')' */
  if( LexemeType == LEX_T_CHARACTER && LexemeChar == '('  ){
    NextLexeme;
    q = ex_Expression_i(pEx,pEx->MAXPREC);
    if( LexemeType != LEX_T_CHARACTER || LexemeChar != ')' ){
      REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_PAREN,NULL);
      }else{
      NextLexeme;
      }
    return q;
    }

  /* check declared constant, like

     const MyConstant = 1
                                       */
  /*--
     This piece of code checks if the current symbol is declared as local or global constant.
     First local constants are checked and then global constant. Locality and globality is done
     the same way as for labels.

     If a symbol is not a defined constant is_const remains zero and nothing happens, life goes on
     normal.

     If a symbol is a constant then the variable pConstantLexeme will
     point to the lexeme of the constant.
   */
  pConstantLexeme = NULL ;
  if( LexemeType == LEX_T_ASYMBOL ){
    if( ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx) )goto ConstFinish;
    strcat(pEx->Buffer,"'");
    if( pEx->ThisFunction ){
      if( strlen(pEx->Buffer) + strlen(pEx->ThisFunction->FunctionName) >= pEx->cbBuffer )
        goto ConstFinish;
      strcat(pEx->Buffer,pEx->ThisFunction->FunctionName );
      pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                                 pEx->GlobalConstants, /* in this table */
                                 0,                 /* do not insert the symbol as new */
                                 alloc_Alloc,
                                 alloc_Free,
                                 pEx->pSymbolTableMemorySegment);
      if( pSymbol ){
        pConstantLexeme = (pLexeme)*pSymbol;
        goto ConstFinish;
        }
      }
    /* we get here if this is not a local constant */
    for( s=pEx->Buffer ; *s && *s != '\'' ; s++ );
    if( *s )s++;
    if( *s )*s = (char)0; /* cut off the function name, try the global const if there is */
    pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                               pEx->GlobalConstants, /* in this table */
                               0,                 /* do not insert the symbol as new */
                               alloc_Alloc,
                               alloc_Free,
                               pEx->pSymbolTableMemorySegment);
    if( pSymbol ){
      pConstantLexeme = (pLexeme)*pSymbol;
      goto ConstFinish;
      }
    /* we get here if this is not a local symbol and is not module symbol try a global one */
    pSymbol = sym_LookupSymbol(LexemeSymbol, /* the symbol we search */
                               pEx->GlobalConstants, /* in this table */
                               0,                 /* do not insert the symbol as new */
                               alloc_Alloc,
                               alloc_Free,
                               pEx->pSymbolTableMemorySegment);
    if( pSymbol ){
      pConstantLexeme = (pLexeme)*pSymbol;
      goto ConstFinish;
      }
    }
ConstFinish:

  /* number or string */
  if( LexemeType == LEX_T_DOUBLE ||
      LexemeType == LEX_T_LONG   ||
      LexemeType == LEX_T_STRING ||
      pConstantLexeme
     ){

    if( pConstantLexeme == NULL )
      pConstantLexeme = pEx->pLex->pLexCurrentLexeme;

    q = new_eNODE();
    if( q == NULL )return NULL;
    switch( pConstantLexeme->type ){
      case LEX_T_DOUBLE:
        q->OpCode = eNTYPE_DBL;
        q->Parameter.Constant.Value.dValue = pConstantLexeme->value.dValue;
        break;
      case LEX_T_LONG:
        q->OpCode = eNTYPE_LNG;
        q->Parameter.Constant.Value.lValue = pConstantLexeme->value.lValue;
        break;
      case LEX_T_STRING: 
        q->OpCode = eNTYPE_STR;
        s = (char *)alloc_Alloc((pConstantLexeme->sLen+1)*sizeof(char),pEx->pMemorySegment);
        if( s == NULL ){
          alloc_Free(q,pEx->pMemorySegment);
          return NULL;
          }
        memcpy(s,pConstantLexeme->value.sValue,pConstantLexeme->sLen+1);
        pEx->cbStringTable += pConstantLexeme->sLen+1;
        COUNT_STRING_LEN
        q->Parameter.Constant.Value.sValue = s;
        q->Parameter.Constant.sLen = pConstantLexeme->sLen;
        break;
      default:
        REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_INTERNAL,NULL);
        break;
        }
    NextLexeme;
    return q;
    }

  /* variable or variable [ '[' expression_list ']' ]  or func '(' expression_list ')' */
   if( LexemeType == LEX_T_ASYMBOL ){
    q = new_eNODE();
    if( q == NULL )return NULL;
    ex_ConvertName(pszFN=LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);

    NextLexeme;
    if( LexemeType == LEX_T_CHARACTER && LexemeChar == '(' ){/* this is user function */
      pSymbol = ex_LookupUserFunction(pEx,1);
      if( *pSymbol == NULL ){
        /* This function has not been defined. */
        *pSymbol = (void *)new_SymbolUF();
        if( *pSymbol == NULL )return NULL;
        ((pSymbolUF)*pSymbol)->FunctionName = pszFN;
        }
      q->OpCode = eNTYPE_FUN;
      q->Parameter.UserFunction.pFunction = (pSymbolUF)(*pSymbol);
      NextLexeme;
      if( LexemeType == LEX_T_CHARACTER && LexemeChar == ')' ){
        /* empty parameter list */
        q->Parameter.UserFunction.Argument = NULL;
        NextLexeme;
        }else{
        q->Parameter.UserFunction.Argument = ex_ExpressionList(pEx);
        if( LexemeType != LEX_T_CHARACTER || LexemeChar != ')' ){/* the closing ) is missing after function call */
          REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_PAREN,NULL);
          }else{ NextLexeme; }
        }
      return q;
      }
    if( LexemeType == LEX_T_CHARACTER && (LexemeChar == '[' || LexemeChar == '{') ){/* this is some array access */
      if( LexemeChar == '[' )is_assoc = 0; else is_assoc = 1;
      NextLexeme;
      q->OpCode = is_assoc ? eNTYPE_SAR : eNTYPE_ARR;
      if( WeAreNotLocal || (pSymbol = ex_LookupLocalVariable(pEx,0)) == NULL ){
        if( WeAreLocal && DefaultLocal && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL ){
          pSymbol = ex_LookupLocalVariable(pEx,1);
          is_local = 1; 
          }else{
          pSymbol = ex_LookupGlobalVariable(pEx,1);
          is_local = 0; 
          }
        }else is_local = 1;

      if( *pSymbol == NULL ){/* this is a new variable symbol */
        if( DeclareVars )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
      if( DefaultLocal && is_local )
          *pSymbol = (void *)new_SymbolVAR(LOCAL_VAR);
        else
          *pSymbol = (void *)new_SymbolVAR(GLOBAL_VAR);
        if( *pSymbol == NULL )return NULL;
        }else{/* this is an existing symbol */
        if( DeclareVars && DefaultLocal && is_local && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL && ex_LookupLocalVariable(pEx,0) == NULL)
          REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
        }
      /* the first argument is the variable */
      q->Parameter.Arguments.Argument = new_eNODE_l();
      if( q->Parameter.Arguments.Argument == NULL )return NULL;
      q->Parameter.Arguments.Argument->actualm = new_eNODE();
      if( q->Parameter.Arguments.Argument->actualm == NULL )return NULL;
      q->Parameter.Arguments.Argument->actualm->Parameter.Variable.Serial = ((pSymbolVAR)(*pSymbol))->Serial;
      q->Parameter.Arguments.Argument->actualm->OpCode = is_local ? eNTYPE_LVR : eNTYPE_GVR;
      /* the rest of the arguments are the indices */
      q->Parameter.Arguments.Argument->rest = ex_ExpressionList(pEx);
      if( is_assoc )
        if( LexemeType != LEX_T_CHARACTER || LexemeChar != '}' ){/* the closing } is missing after array indexes */
          REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_SAPAREN,NULL);
          }else{ NextLexeme; }
      else
        if( LexemeType != LEX_T_CHARACTER || LexemeChar != ']' ){/* the closing ] is missing after array indexes */
          REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_APAREN,NULL);
          }else{ NextLexeme; }
      /* now process the additional indices, like a[13]{"alma"}[5] */
      while( LexemeType == LEX_T_CHARACTER && (LexemeChar == '[' || LexemeChar == '{') ){
        if( LexemeChar == '[' )is_assoc = 0; else is_assoc = 1;
        NextLexeme;
        r = q;
        q = new_eNODE();
        if( q == NULL )return NULL;
        q->OpCode = is_assoc ? eNTYPE_SAR : eNTYPE_ARR;
        q->Parameter.Arguments.Argument = new_eNODE_l();
        if( q->Parameter.Arguments.Argument == NULL )return NULL;
        /* The first element of the list is the array up to here, the rest is the actual index list. */
        q->Parameter.Arguments.Argument->actualm = r;
        /* the rest of the arguments are the indices */
        q->Parameter.Arguments.Argument->rest = ex_ExpressionList(pEx);
        if( is_assoc )
          if( LexemeType != LEX_T_CHARACTER || LexemeChar != '}' ){/* the closing } is missing after array indexes */
            REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_SAPAREN,NULL);
            }else{ NextLexeme; }
        else
          if( LexemeType != LEX_T_CHARACTER || LexemeChar != ']' ){/* the closing ] is missing after array indexes */
            REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_APAREN,NULL);
            }else{ NextLexeme; }
        }
      return q;
      }

    /* this is a simple variable */
    if( WeAreNotLocal || (pSymbol = ex_LookupLocalVariable(pEx,0)) == NULL ){
      if( WeAreLocal && DefaultLocal && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL ){
        /* if we are local, the variable is not 
           declared and by default all undeclared variables are local */
        pSymbol = ex_LookupLocalVariable(pEx,1);
        is_local = 1;
        }else{
        /* if we are global or undeclared variables are global */
        pSymbol = ex_LookupGlobalVariable(pEx,1);
        is_local = 0;
        }
      }else is_local = 1;
    if( *pSymbol == NULL ){/* this is a new variable symbol */
      if( DeclareVars )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
    if( DefaultLocal && is_local )
        *pSymbol = (void *)new_SymbolVAR(LOCAL_VAR);
      else
        *pSymbol = (void *)new_SymbolVAR(GLOBAL_VAR);
      if( *pSymbol == NULL )return NULL;
      }else{/* this is an existing symbol */
      if( DeclareVars && DefaultLocal && is_local && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL && ex_LookupLocalVariable(pEx,0) == NULL)
        REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
      }
    q->OpCode = is_local ? eNTYPE_LVR : eNTYPE_GVR;
    q->Parameter.Variable.Serial = ((pSymbolVAR)(*pSymbol))->Serial;
    return q;
    }
   return NULL;
}

/*POD
=H ex_Expression_i

This function is called to analyze a sub-expression that has no lower precedence operators
than T<i> (unless enclosed in parentheses inside the sub expression).

If the argument variable T<i> is T<1> then this function simply calls R<ex_Tag>. Otherwise it
calls itself recursively twice with optionally compiling the operator between the 
two subexpressions.

/*FUNCTION*/
peNODE ex_Expression_i(peXobject pEx,
                       int i
  ){
/*noverbatim
The function returns pointer to the new node.
CUT*/
  peNODE fo; /* first operand */
  peNODE q;
  int iOperation;

  if( i == 1 )return ex_Tag(pEx);

  fo = ex_Expression_i(pEx,i-1); /* first operand */
  if( fo == NULL )return NULL;
  while( iOperation = ex_IsBinop(pEx,i-1) ){
    q = new_eNODE();
    if( q == NULL )return NULL;
    q->OpCode = iOperation;
    q->Parameter.Arguments.Argument = new_eNODE_l();
    if( q->Parameter.Arguments.Argument == NULL )return NULL;
    q->Parameter.Arguments.Argument->actualm = fo;
    q->Parameter.Arguments.Argument->rest = new_eNODE_l();
    if( q->Parameter.Arguments.Argument->rest == NULL )return NULL;
    NextLexeme;
    q->Parameter.Arguments.Argument->rest->actualm = ex_Expression_i(pEx,i-1);
    /*this was inserted for v1.0b20 to report error on expression having no right argument for an op, like
      if a= then 
    */
    if( q->Parameter.Arguments.Argument->rest->actualm == NULL )return NULL;
    fo = q;
    }
  return fo;
}

/*POD
=H ex_Expression_r

This function implements the syntax analysis for an expression. This is quite simple. It only
calls R<ex_Expression_i> to handle the lower precendece expression. 
/*FUNCTION*/
void ex_Expression_r(peXobject pEx,
                     peNODE *Result
  ){
/*noverbatim
CUT*/
  *Result = ex_Expression_i(pEx,pEx->MAXPREC);
  }

/*POD
=H ex_IsSymbolValidLval(pEx)

This function checks whether the actual symbol used in as a start symbol of a left value
is defined as a CONST in the BASIC program or not. If this is a const then the syntax analizer
has to report an error (since v1.0b31).

This function is called from the function R<ex_LeftValue> after the symbol was name space corrected.

Note that a symbol can be a global, name space independant constant, a name space local constant and
a function local constant. All these differ only in name decoration inside the interpreter.

If a symbol is a local variable but is also a module or global symbol, but is NOT a function local symbol
then that variable can indeed stand on the left side of a LET command. Therefore we check if the symbol
is in the local variables table and in case this is in some of the global or module contant table,
we just do not care.
/*FUNCTION*/
int ex_IsSymbolValidLval(peXobject pEx
  ){
/*noverbatim
The function returns 1 if the symbol is a constant or zero if not.
CUT*/
  void **pSymbol;
  char *s;
  char *fs;
  int isLocalVar;

  fs = pEx->Buffer + strlen(pEx->Buffer);
  if( pEx->iWeAreLocal && pEx->ThisFunction ){
    pSymbol = sym_LookupSymbol(pEx->Buffer,        /* symbol we search */
                               pEx->LocalVariables,/* in this table */
                               0,                  /* dont insert if this is not a local symbol */
                               alloc_Alloc,
                               alloc_Free,
                               pEx->pLocalVarMemorySegment);
    isLocalVar = NULL != pSymbol; /* this may be a local variable if it is in the symbol table,
                                     thoug let's check if this is a local constant */
    }else isLocalVar = 0; /* can not be a local variable in a global environment */

  strcpy(fs,"'");
  if( pEx->iWeAreLocal && pEx->ThisFunction ){
    if( strlen(pEx->Buffer) + strlen(pEx->ThisFunction->FunctionName) >= pEx->cbBuffer )
      return 0;
    strcat(pEx->Buffer,pEx->ThisFunction->FunctionName );
    pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                                pEx->GlobalConstants, /* in this table */
                                0,                 /* do not insert the symbol as new */
                                alloc_Alloc,
                                alloc_Free,
                                pEx->pSymbolTableMemorySegment);
    if( pSymbol && *pSymbol )return 1; /* this is a local constant */
    }

  /* If this is a local var then no further checks are needed. This is a local variable and that is it. */
  if( isLocalVar ){
    /* restore the symbol name cutting off all that has been appended, the ' and the function name */
    *fs = (char)0;
    return 0;/* this is not a const, this is a valid lval symbol */
    }

  /* we get here if this is not a function local constant nor a declared local variable */
  s = fs + 1 ;
  if( *s )*s = (char)0; /* cut off the function name, try the global const if there is */
  pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                             pEx->GlobalConstants, /* in this table */
                             0,                 /* do not insert the symbol as new */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pSymbolTableMemorySegment);
  if( pSymbol && *pSymbol )return 1;/* this is a module symbol */

  /* we get here if this is not a local symbol and is not module symbol try a global one */
  pSymbol = sym_LookupSymbol(LexemeSymbol, /* the symbol we search */
                              pEx->GlobalConstants, /* in this table */
                              0,                 /* do not insert the symbol as new */
                              alloc_Alloc,
                              alloc_Free,
                              pEx->pSymbolTableMemorySegment);
  if( pSymbol && *pSymbol )return 1;

  /* restore the symbol name cutting off all that has been appended */
  *fs = (char)0;
  /* give it up, this is not a const... */
  return 0;
  }

/*POD
=H ex_LeftValue

This function implements the syntax analisys for a left value.

/*FUNCTION*/
peNODE ex_LeftValue(peXobject pEx
  ){
/*noverbatim
The function returns pointer to the new node.
CUT*/
  peNODE q,r;
  int is_local;
  int is_assoc; /* is the array reference associative? */
  void **pSymbol;

  if( LexemeType != LEX_T_ASYMBOL )return NULL;
  ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
  if( ex_IsSymbolValidLval(pEx) )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_CONST_LVAL,NULL);

  NextLexeme;
  if( LexemeType == LEX_T_CHARACTER && (LexemeChar == '[' || LexemeChar == '{') ){
    if( LexemeChar == '{' )is_assoc = 1; else is_assoc = 0;
    NextLexeme;
    q = new_eNODE();
    if( q == NULL )return NULL;
    q->OpCode = is_assoc ? eNTYPE_SAR : eNTYPE_ARR;
    if( WeAreNotLocal || (pSymbol = ex_LookupLocalVariable(pEx,0)) == NULL ){
      if( WeAreLocal && DefaultLocal && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL ){
        pSymbol = ex_LookupLocalVariable(pEx,1);
        is_local = 1; 
        }else{
        pSymbol = ex_LookupGlobalVariable(pEx,1);
        is_local = 0; 
        }
      }else is_local = 1;

    if( *pSymbol == NULL ){/* this is a new variable symbol */
      if( DeclareVars )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
    if( DefaultLocal && is_local )
        *pSymbol = (void *)new_SymbolVAR(LOCAL_VAR);
      else
        *pSymbol = (void *)new_SymbolVAR(GLOBAL_VAR);
      if( *pSymbol == NULL )return NULL;
      }else{/* this is an existing symbol */
      if( DeclareVars && DefaultLocal && is_local && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL && ex_LookupLocalVariable(pEx,0) == NULL)
        REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
      }
    /* the first argument is the variable */
    q->Parameter.Arguments.Argument = new_eNODE_l();
    if( q->Parameter.Arguments.Argument == NULL )return NULL;
    q->Parameter.Arguments.Argument->actualm = new_eNODE();
    if( q->Parameter.Arguments.Argument->actualm == NULL )return NULL;
    q->Parameter.Arguments.Argument->actualm->Parameter.Variable.Serial = ((pSymbolVAR)(*pSymbol))->Serial;
    q->Parameter.Arguments.Argument->actualm->OpCode = is_local ? eNTYPE_LVR : eNTYPE_GVR;
    /* the rest of the arguments are the indices */
    q->Parameter.Arguments.Argument->rest = ex_ExpressionList(pEx);
    if( is_assoc )
      if( LexemeType != LEX_T_CHARACTER || LexemeChar != '}' ){/* the closing } is missing after array indexes */
        REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_SAPAREN,NULL);
        }else{ NextLexeme; }
    else
      if( LexemeType != LEX_T_CHARACTER || LexemeChar != ']' ){/* the closing ] is missing after array indexes */
        REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_APAREN,NULL);
        }else{ NextLexeme; }
    /* now process the additional indices, like a[13]{"alma"}[5] */
    while( LexemeType == LEX_T_CHARACTER && (LexemeChar == '[' || LexemeChar == '{') ){
      if( LexemeChar == '[' )is_assoc = 0; else is_assoc = 1;
      NextLexeme;
      r = q;
      q = new_eNODE();
      if( q == NULL )return NULL;
      q->OpCode = is_assoc ? eNTYPE_SAR : eNTYPE_ARR;
      q->Parameter.Arguments.Argument = new_eNODE_l();
      if( q->Parameter.Arguments.Argument == NULL )return NULL;
      /* The first element of the list is the array up to here, the rest is the actual index list. */
      q->Parameter.Arguments.Argument->actualm = r;
      /* the rest of the arguments are the indices */
      q->Parameter.Arguments.Argument->rest = ex_ExpressionList(pEx);
      if( is_assoc )
        if( LexemeType != LEX_T_CHARACTER || LexemeChar != '}' ){/* the closing } is missing after array indexes */
          REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_SAPAREN,NULL);
          }else{ NextLexeme; }
      else
        if( LexemeType != LEX_T_CHARACTER || LexemeChar != ']' ){/* the closing ] is missing after array indexes */
          REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_APAREN,NULL);
          }else{ NextLexeme; }
      }
    return q;
    }

  q = new_eNODE();
  if( q == NULL )return NULL;
  /* this is a simple variable */
  if( WeAreNotLocal || (pSymbol = ex_LookupLocalVariable(pEx,0)) == NULL ){
    if( WeAreLocal && DefaultLocal && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL ){
      pSymbol = ex_LookupLocalVariable(pEx,1);
      is_local = 1; 
      }else{
      pSymbol = ex_LookupGlobalVariable(pEx,1);
      is_local = 0; 
      }
    }else is_local = 1;
  if( *pSymbol == NULL ){
    if( DeclareVars )REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
    if( DefaultLocal && is_local )
      *pSymbol = (void *)new_SymbolVAR(LOCAL_VAR);
    else
      *pSymbol = (void *)new_SymbolVAR(GLOBAL_VAR);
    }else{/* this is an existing symbol */
    if( DeclareVars && DefaultLocal && is_local && ex_LookupLocallyDeclaredGlobalVariable(pEx) == NULL && ex_LookupLocalVariable(pEx,0) == NULL)
      REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_GLOBAL,NULL);
    }
  q->OpCode = is_local ? eNTYPE_LVR : eNTYPE_GVR;
  q->Parameter.Variable.Serial = ((pSymbolVAR)(*pSymbol))->Serial;
  return q;
  }

/*POD
=H ex_PredeclareGlobalLongConst()

This function is used to declare the global constants that are given
in the syntax defintinon, and should be defined before the program
is started to be analized.

/*FUNCTION*/
int ex_PredeclareGlobalLongConst(peXobject pEx,
                                 char *pszConstName,
                                 long lConstValue
  ){
/*noverbatim
CUT*/
  void **pSymbol;
  pLexeme pConstValue;
  void *prepar[2];

  prepar[0] = (void *)pszConstName;
  prepar[1] = (void *)lConstValue;
  pConstValue = alloc_Alloc(sizeof(Lexeme),pEx->pSymbolTableMemorySegment);
  if( pConstValue == NULL )return EX_ERROR_MEMORY_LOW;
  pSymbol = sym_LookupSymbol(pszConstName, /* the symbol we search */
                             pEx->GlobalConstants, /* in this table */
                             1,                 /* insert the symbol as new */
                             alloc_Alloc,
                             alloc_Free,
                             pEx->pSymbolTableMemorySegment);
  if( pSymbol == NULL )return 1;
  *pSymbol = (void *)pConstValue; /* note that this const value can be NULL to force a previously*/
  pConstValue->type = LEX_T_LONG;
  pConstValue->value.lValue = lConstValue;
  return 0;
  }

/*POD
=H ex_IsCommandThis

This is the most general syntax analysis function that tries to match the syntax
of the actual line syntax provided in argument T<p> against the token list at the actual
position.

The function has several side effects altering optionally the global and local variable table,
define user defined functions and so on.

The function signals the success of its operation via the argument T<piFailure> setting the T<int>
pointed by it to be zero or the error code.

If the syntax does not match the token list then the function cleans up all its actions if possible
to allow the caller to iterate over to the next syntax defintion. In such a situation
T<*piFailure> is set T<EX_ERROR_SYNTAX>

If the syntax does not match the token list but the analysis went too far and had side effects that
cannot be reversed then no cleanup is made. In such a situation T<*piFailure> is set
T<EX_ERROR_SYNTAX_FATAL>.

T<*piFailure> is also set to this value if the syntax definition reaches a "star" point. If the syntax
analysis matches a line up to a "star" point then the line should match that syntax definition or is
known erroneous. For example a command starting with the two keywords T<'declare' 'command'> after these
two keywords reach a "star" point because no other line syntax but extrenal command declaration starts
with these two keywords. In such a situation signalling fatal syntax error saves the compiler time
to check other syntax definition.

A "star" point is named this way, because the file T<syntax.def> uses the character T<*> to denote
this point in the syntax definitions.

/*FUNCTION*/
peNODE ex_IsCommandThis(peXobject pEx,
                        pLineSyntax p,
                        int *piFailure
  ){
/*noverbatim
If the syntax analysis fully matches the syntax definition provided in the argument
then the function returns the node that was generated. If more then one nodes were generated
during the syntax analysis of the line then the root node of the generated nodes is returned.
CUT*/
#define ABORT goto SYNTAX_FAILURE_DO_CLEANUP
#define ARGUMENT pArgument->Parameter.CommandArgument.Argument
#define ASSERT_NON_NULL(x) if( (x) == NULL ){ *piFailure = EX_ERROR_MEMORY_LOW; ABORT; }

#define NewArgument if( (*ppArgument = new_eNODE()) == NULL ){\
                       *piFailure = EX_ERROR_MEMORY_LOW;\
                       ABORT;\
                       }else{\
                       pArgument = *ppArgument;\
                       ppArgument = &(pArgument->Parameter.CommandArgument.next);\
                       *ppArgument = NULL;\
                       }
/*
This function call uses the stored memory segment because the memory is not release
in case of error, but stored in a local free list. This caused error in former versions
because the segment was released, but the local list still used the same memory. */
#define ex_PushLabel(y,z) _ex_PushLabel(pEx,y,z,pMyMemorySegment)

  void *pMyMemorySegment,*pSwapMemorySegment;
#define ex_SwapMemorySegment() do{ pSwapMemorySegment  = pEx->pMemorySegment; \
                                   pEx->pMemorySegment = pMyMemorySegment;    \
                                   pMyMemorySegment    = pSwapMemorySegment; }while(0)
  int iCurrentLex;
  int iSaveWeAreLocal;
  peNODE pCommandNode;
  peNODE *ppArgument,pArgument;
  pSymbolUF pFunction;
  pSymbolLABEL pLabel;
  void **pSymbol;
  void **pFailedFunctionSymbol;
  char *pszNewNameSpace;
  char *pszLabelDefined;
  char *pszConstDefined;
  int iConstGlobal;
  pLexeme pConstValue;
  char szNumericLabelName[80]; /* who is writing 10000...00 such a long basic label ?*/
  int iSideEffectWas;
  int iCommandNeedsCode;
  int fResetNameSpace;
  int StackCleanc;
  int isig;
  long sLen;

  iCommandNeedsCode = 1; /* command needs code even if there are no argument of the command */
  fResetNameSpace = 0;
  *piFailure = EX_ERROR_SUCCESS;
  iSaveWeAreLocal     = pEx->iWeAreLocal;
  pszNewNameSpace     = NULL;
  pszLabelDefined     = NULL;
  pszConstDefined     = NULL;
  pConstValue         = NULL;
  iSideEffectWas      = 0; /* there was no side effect so far */
  StackCleanc = 0;
  pFailedFunctionSymbol = NULL;
  pMyMemorySegment    = pEx->pMemorySegment; /* store the old value */
  /* allocate a new segment which is dropped on failure or merged on success */
  pEx->pMemorySegment = alloc_InitSegment(pEx->memory_allocating_function,
                                       pEx->memory_releasing_function);

  if( pEx->pMemorySegment == NULL ){
    pEx->pMemorySegment = pMyMemorySegment;
    return NULL;
    }
  ppArgument = &pCommandNode;
  pCommandNode = NULL;

  for( iCurrentLex = 0; p->lexes[iCurrentLex].type ; iCurrentLex ++ ){
    switch( p->lexes[iCurrentLex].type ){

      case EX_LEX_LOCAL_START: /* start local scope */
        if( pEx->iWeAreLocal )ABORT;/* this is some nested function construct,not allowed */
        pEx->iWeAreLocal = 1;
        pEx->cLocalVariables  = 0;
        NewArgument;
        pEx->plNrLocalVariables = &(ARGUMENT.lLongValue);
        pEx->LocalVariables = sym_NewSymbolTable(alloc_Alloc,pEx->pLocalVarMemorySegment);
        pEx->LocallyDeclaredGlobalVariables = sym_NewSymbolTable(alloc_Alloc,pEx->pLocalVarMemorySegment);
        CALL_PREPROCESSOR(PreprocessorExStartLocal,pEx);
        break;

      case EX_LEX_ARG_NUM: /* store the number of arguments */
        NewArgument;
        ARGUMENT.lLongValue = pEx->cLocalVariables;
        break;

      case EX_LEX_LOCAL_END: /* finish local scope */
        if( pEx->plNrLocalVariables )/* this may be NULL when a syntax error occured */
          *(pEx->plNrLocalVariables) = pEx->cLocalVariables;
        pEx->plNrLocalVariables = NULL; /* just to be safe */
        pEx->iWeAreLocal = 0;
        CALL_PREPROCESSOR(PreprocessorExEndLocal,pEx);
        /* there are other actions when the whole line is matched */
        break;

     case EX_LEX_STAR:
        iSideEffectWas = 1;
        break;

     case EX_LEX_NOEXEC:
        iCommandNeedsCode = 0;
        break;
/*
       NOTE that local variables ARE inserted into the symbol table during the evaluation.
       This happens even if the matching process fails. This means a restriction on the
       normal syntax defintions. The syntax should know that this is a valid line and this
       is a local variable defintion when the analysis gets here.
*/
      case EX_LEX_LOCAL:    /* local variable definition */
        iSideEffectWas = 1;
        if( ex_Local(pEx) )ABORT;
        break;

      case EX_LEX_LOCALL:   /* local variable definition list*/
        iSideEffectWas = 1;
        if( ex_LocalList(pEx) )ABORT;
        break;

      case EX_LEX_GLOBAL: /* global variable definition */
        iSideEffectWas = 1;
        if( ex_Global(pEx) )ABORT;
        break;

      case EX_LEX_GLOBALL:   /* global variable definition list*/
        iSideEffectWas = 1;
        if( ex_GlobalList(pEx) )ABORT;
        break;

/*
       NOTE that analyzing an expression or left value has a lot of side effects therefore the same restrictions
       will apply for expressions as does for LOCAL and LOCALL.
*/

      case EX_LEX_EXP:      /* expression */
        iSideEffectWas = 1;
        NewArgument;
        ex_Expression_r(pEx,&(ARGUMENT.pNode));
        if( ARGUMENT.pNode == NULL )ABORT;
        break;

      case EX_LEX_EXPL:     /* expression list */
        iSideEffectWas = 1;
        NewArgument;
        if( (ARGUMENT.pNodeList = ex_ExpressionList(pEx)) != NULL ){
          break; 
          }else{
          ABORT;
          }

/* Generally the LET instruction that starts with a left value should be placed at the end of
   the command definition list. This is because left value analysis makes some side effects,
   like inserting undeclared global variables into the symbol table. Therefore when an LVAL
   syntax element is reached during syntax analysis it should be sure that the instruction is
   the one that is currently checked or is syntactically incorrect.

   However this is a very strict rule, and to ease the syntax defintion table build up we
   check that the very first lexeme that comes when a left value (or list of lvals)
   is indeed a symbol. If this is not a symbol, then nothing fatal has happened, and we can abort the
   check against the current syntax defintion line without prohibiting the syntax analyzer to
   go on for other lines.
*/

      case EX_LEX_LVAL:     /* left value */
        if( LexemeType != LEX_T_ASYMBOL )ABORT; /* this is needed only to ease a bit 
                                                   syntax defintion table build up. */
        iSideEffectWas = 1;
        NewArgument;
        if( (ARGUMENT.pNode = ex_LeftValue(pEx)) != NULL ){
          break;
          }else{
          ABORT;
          }

      case EX_LEX_LVALL:     /* left value list */
        if( LexemeType != LEX_T_ASYMBOL )ABORT; /* this is needed only to ease a bit 
                                                   syntax defintion table build up. */
        iSideEffectWas = 1;
        NewArgument;
        if( (ARGUMENT.pNodeList = ex_LeftValueList(pEx)) != NULL ){
          break;
          }else{
          ABORT;
          }
/*
       NOTE that COME and GO virtual syntax elements push or pop values to/from the compile time stack.
       Therefore these elements should only appear at the end of the syntax defintion line where it is sure
       that the line is going to be accepted by this syntax definition.
*/
      case EX_LEX_GO_FORWARD:
        iSideEffectWas = 1;
        NewArgument;
        pLabel = (ARGUMENT.pLabel = new_SymbolLABEL());
        ex_PushLabel(pLabel,p->lexes[iCurrentLex].GoConstant[0]);
        StackCleanc++;
        break;

      case EX_LEX_GO_BACK:
        iSideEffectWas = 1;
        NewArgument;
        pLabel = (ARGUMENT.pLabel = ex_PopLabel(p->lexes[iCurrentLex].GoConstant));
        break;

      case EX_LEX_COME_FORWARD:
        iSideEffectWas = 1;
        pLabel = ex_PopLabel(p->lexes[iCurrentLex].GoConstant);
        /* Kevin Landman proposed that this check has to be done here to detect ELSE w/o any IF */
        if( NULL == pLabel ){
	        *piFailure = EX_ERROR_BAD_NESTING;
	        ABORT;
          }
        ex_PushWaitingLabel(pEx,pLabel);
        break;

      case EX_LEX_COME_BACK:
        iSideEffectWas = 1;
        pLabel = new_SymbolLABEL();
        ex_PushWaitingLabel(pEx,pLabel);
        ex_PushLabel(pLabel,p->lexes[iCurrentLex].GoConstant[0]);
        StackCleanc++;
        break;

      case EX_LEX_NSYMBOL: /* alpha or non-alpha symbol to ease handling and to let define alpha alternatives for non-alpha symbols */
        if( LexemeType != LEX_T_NSYMBOL )ABORT;
        /* printf("%s =?= %s\n",lex_SymbolicName(pEx->pLex,LexemeCode),lex_SymbolicName(pEx->pLex,p->lexes[iCurrentLex].OpCode)); */
        if( LexemeCode != p->lexes[iCurrentLex].OpCode )ABORT;
        NextLexeme;
        break;

      case EX_LEX_FUNCTION: /* a symbol that stands for a function or procedure name when function or
                               procedure is defined */
        if( LexemeType != LEX_T_ASYMBOL )ABORT;
        *piFailure = ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
        if( *piFailure )ABORT;
        pSymbol = ex_LookupUserFunction(pEx,1);
        ASSERT_NON_NULL(pSymbol);
        if( *pSymbol == NULL ){
          pFailedFunctionSymbol = pSymbol; /* we need this in case the command fails. */
          *pSymbol = (void *)(pFunction=new_SymbolUF());
          pFunction->node = 0; /* not defined yet */
          }
        else{
          pFunction = (pSymbolUF)*pSymbol;
          pFailedFunctionSymbol = NULL;
          }
        ASSERT_NON_NULL(pFunction)
        pEx->pFunctionWaiting = pFunction;
        pEx->ThisFunction = pFunction;
        pEx->ThisFunction->Argc = -1;
        pEx->ThisFunction->FunctionName = LexemeSymbol;
        if( pFunction->node ){
          *piFailure = EX_ERROR_FUNCTION_DOUBLE_DEFINED;
          ABORT;
          }
        NextLexeme;
        break;

      case EX_LEX_THIS_FUNCTION: /* a symbol that stands for a function or procedure name */
        if( LexemeType != LEX_T_ASYMBOL )ABORT;
        ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
        pSymbol = ex_LookupUserFunction(pEx,0);
        if( pSymbol == NULL )ABORT; /* no this is not a function name */
        pFunction = (pSymbolUF)*pSymbol;
        if( pEx->ThisFunction == NULL || 
            pFunction->FunId != pEx->ThisFunction->FunId )ABORT; /* this is a function name, but not the current */
        NextLexeme;
        break;

      case EX_LEX_CONST_NAME: /* a const is going to be defined (symbol) */
        if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_LONG )ABORT;
        pszConstDefined = LexemeSymbol; /* there can only be one const defined on a line */
        /* the real const defintion takes place when the whole line is matched */
        NextLexeme;
        iConstGlobal = 0;
        break;

      case EX_LEX_GCONST_NAME: /* a global const is going to be defined (symbol) */
        if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_LONG )ABORT;
        pszConstDefined = LexemeSymbol; /* there can only be one const defined on a line */
        /* the real const defintion takes place when the whole line is matched */
        NextLexeme;
        iConstGlobal = 1;
        break;

      case EX_LEX_CONST_VALUE:
        /* Here we have to let the user specify "simple exppression" as constant.
           The string -nnn or +nnn is never recognized as signed number by the lexical analyzer.
           It should not recognize it as signed number, because in that case we could face problems
           with expressions like 6+3. Then it would be just two numbers one following the other
           instead of being an expression. Therefore here we let the user to use a simple expression
           as constant value. If the lexical element that stands in place of the CVAL is a + or - sign
           then we go forward and take the number (if it is a number) and alter it according to the
           sign.
         */
        if( LexemeType == LEX_T_NSYMBOL && ( LexemeCode == CMD_MINUS || LexemeCode == CMD_PLUS ) ){
          pConstValue = pEx->pLex->pLexCurrentLexeme; /* in case the next symbol is not a number */
          if( LexemeCode == CMD_MINUS )isig = -1; else isig = 1;
          NextLexeme;
          if( LexemeType != LEX_T_DOUBLE && LexemeType != LEX_T_LONG )ABORT;
          pConstValue = pEx->pLex->pLexCurrentLexeme;
          NextLexeme;
          if( LexemeType != LEX_T_DOUBLE ){
            pConstValue->value.lValue *= isig;
            }else{
            pConstValue->value.dValue *= isig;
            }
          break;
          }
        if(  LexemeType != LEX_T_DOUBLE && LexemeType != LEX_T_LONG && LexemeType != LEX_T_STRING )ABORT;
        pConstValue = pEx->pLex->pLexCurrentLexeme;
        NextLexeme;
        break;

      case EX_LEX_LABEL_DEFINITION: /* a global label is defined (symbol) */
        if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_LONG )ABORT;
        if( LexemeType == LEX_T_ASYMBOL ){
          pszLabelDefined = LexemeSymbol; /* there can only be one label defined on a line */
          /* the real label defintion takes place when the whole line is matched */
          }else{
          sprintf(szNumericLabelName,"%ld",LexemeLong);
          pszLabelDefined = szNumericLabelName;
          }
        NextLexeme;
        break;

      case EX_LEX_LABEL: /* a global label is used */
        if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_LONG)ABORT;
        if( LexemeType == LEX_T_ASYMBOL ){
          *piFailure = ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
          }else{
          sprintf(szNumericLabelName,"%ld",LexemeLong);
          *piFailure = ex_ConvertName(szNumericLabelName, pEx->Buffer,pEx->cbBuffer,pEx);
        }
        
        if( *piFailure )ABORT;/* This is memory fault. */
        iSideEffectWas = 1; /* we insert this into the global label table */
        if( strlen(pEx->Buffer) >= pEx->cbBuffer-1 ){
          *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
          ABORT;
          }
        strcat(pEx->Buffer,"'");
        if( pEx->ThisFunction ){
          if( strlen(pEx->Buffer) + strlen(pEx->ThisFunction->FunctionName) >= pEx->cbBuffer ){
            *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
            ABORT;
            }
          strcat(pEx->Buffer,pEx->ThisFunction->FunctionName );
          }

        pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                                   pEx->GlobalLabels, /* in this table */
                                   1,                 /* insert the symbol as new */
                                   alloc_Alloc,
                                   alloc_Free,
                                   pEx->pSymbolTableMemorySegment);
        ASSERT_NON_NULL(pSymbol)
        if( *pSymbol == NULL ){
          *pSymbol = (void *)new_SymbolLABEL();
          pLabel = (pSymbolLABEL)*pSymbol;
          pLabel->node = 0;
          }else
          pLabel = (pSymbolLABEL)*pSymbol;
        ASSERT_NON_NULL(pLabel)
        NewArgument;
        ARGUMENT.pLabel = pLabel;
        NextLexeme;
        break;

      case EX_LEX_SET_NAME_SPACE: /* set the new name space */
        if( LexemeType != LEX_T_ASYMBOL )ABORT;
        if( LexemeSymbol[0] == ':' && LexemeSymbol[1] == ':' ){
          /* you should never have a syntax that has name space alteration and other stuff that uses the pEx buffer */
          *piFailure = ex_ConvertName(LexemeSymbol+2, pEx->Buffer,pEx->cbBuffer,pEx);
          if( *piFailure )ABORT;
          pszNewNameSpace = pEx->Buffer;
          }else{
          pszNewNameSpace = LexemeSymbol;  /* we will set is later, when the line has been accepted */
          }
        NextLexeme;
        break;

      case EX_LEX_RESET_NAME_SPACE: /*  reset the name space to the old value */
        fResetNameSpace = 1;
        break;

      case EX_LEX_PRAGMA:
        if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_NSYMBOL )ABORT;
        if( LexemeType == LEX_T_ASYMBOL ){
          ex_Pragma(pEx,LexemeSymbol);
          }else{/* this is to handle absolute symbols that are predefined */
          ex_Pragma(pEx,lex_SymbolicName(pEx->pLex,LexemeCode));
          }
        NextLexeme;
        break;

      /* an absolute symbol without name space modification */
      case EX_LEX_ASYMBOL:
        if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_NSYMBOL )ABORT;
        if( LexemeType == LEX_T_ASYMBOL ){
          NewArgument;
          if( (ARGUMENT.szStringValue = alloc_Alloc(sLen=strlen(LexemeSymbol)+1,pEx->pMemorySegment)) == NULL )ABORT;
          strcpy(ARGUMENT.szStringValue,LexemeSymbol);
          pArgument->Parameter.CommandArgument.sLen = sLen;
          pEx->cbStringTable += sLen+1;
          COUNT_STRING_LEN
          }else{/* this is to handle absolute symbols that are predefined */
          NewArgument;
          if( (ARGUMENT.szStringValue = alloc_Alloc(sLen=strlen(lex_SymbolicName(pEx->pLex,LexemeCode))+1,pEx->pMemorySegment)) == NULL )ABORT;
          strcpy(ARGUMENT.szStringValue,lex_SymbolicName(pEx->pLex,LexemeCode));
          pArgument->Parameter.CommandArgument.sLen = sLen;
          pEx->cbStringTable += sLen+1;
          COUNT_STRING_LEN
          }
        NextLexeme;
        break;

      case EX_LEX_SYMBOL:   /* a symbol, like an external function name from a dll */
        if( LexemeType != LEX_T_ASYMBOL )ABORT;
        *piFailure = ex_ConvertName(LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
        if( *piFailure )ABORT;
        NewArgument;
        if( (ARGUMENT.szStringValue = alloc_Alloc((sLen=strlen(pEx->Buffer))+1,pEx->pMemorySegment)) == NULL )ABORT;
        strcpy(ARGUMENT.szStringValue,pEx->Buffer);
        pArgument->Parameter.CommandArgument.sLen = sLen;
        pEx->cbStringTable += sLen+1;
        COUNT_STRING_LEN
        NextLexeme;
        break;

      case EX_LEX_CHARACTER: /* a character, like '(' or ')' */
        if( LexemeType == LEX_T_CHARACTER && p->lexes[iCurrentLex].OpCode == LexemeChar ){
          NextLexeme;
          break;
          }
        ABORT;

      case EX_LEX_LONG:     /* a numeric integer value */
        if( LexemeType != LEX_T_LONG )ABORT;
        NewArgument;
        ARGUMENT.lLongValue = LexemeLong;
        NextLexeme;
        break;

      case EX_LEX_DOUBLE:   /* a numeric float value */
        if( LexemeType != LEX_T_DOUBLE && LexemeType != LEX_T_LONG )ABORT;
        NewArgument;
        if( LexemeType == LEX_T_LONG )
          ARGUMENT.dDoubleValue = (double)LexemeLong;
        else
          ARGUMENT.dDoubleValue = LexemeDouble;
        NextLexeme;
        break;

      case EX_LEX_STRING:   /* a string value */
        if( LexemeType != LEX_T_STRING )ABORT;
        NewArgument;
        ASSERT_NON_NULL( (ARGUMENT.szStringValue = alloc_Alloc(LexemeStrLen+1,pEx->pMemorySegment)) )
        memcpy(ARGUMENT.szStringValue,LexemeSymbol,LexemeStrLen+1);
        pArgument->Parameter.CommandArgument.sLen = LexemeStrLen;
        pEx->cbStringTable += LexemeStrLen+1;
        COUNT_STRING_LEN
        NextLexeme;
        break;
      }
    }

  /* if a const was defined on the line */
  if( pszConstDefined ){
    if( iConstGlobal ){
      if( strlen(pszConstDefined) >= pEx->cbBuffer ){
        *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
        ABORT;
        }
      strcpy(pEx->Buffer,pszConstDefined);
      }else{
      ex_ConvertName(pszConstDefined, pEx->Buffer,pEx->cbBuffer,pEx);
      if( strlen(pEx->Buffer) >= pEx->cbBuffer-1 ){
        *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
        ABORT;
        }
      strcat(pEx->Buffer,"'");
      if( pEx->ThisFunction ){
        if( strlen(pEx->Buffer) + strlen(pEx->ThisFunction->FunctionName) >= pEx->cbBuffer ){
          *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
          ABORT;
          }
        strcat(pEx->Buffer,pEx->ThisFunction->FunctionName );
        }
      }

    pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                               pEx->GlobalConstants, /* in this table */
                               1,                 /* insert the symbol as new */
                               alloc_Alloc,
                               alloc_Free,
                               pEx->pSymbolTableMemorySegment);
    ASSERT_NON_NULL(pSymbol)
    *pSymbol = (void *)pConstValue; /* note that this const value can be NULL to force a previously
                                       declared constant to be variable again */
    }

  /* if a label was defined on the line do all the tasks that have side effects only now. */
  if( pszLabelDefined ){
    ex_ConvertName(pszLabelDefined, pEx->Buffer,pEx->cbBuffer,pEx);
    if( strlen(pEx->Buffer) >= pEx->cbBuffer-1 ){
      *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
      ABORT;
      }
    strcat(pEx->Buffer,"'");
    if( pEx->ThisFunction ){
      if( strlen(pEx->Buffer) + strlen(pEx->ThisFunction->FunctionName) >= pEx->cbBuffer ){
        *piFailure = EX_ERROR_TOO_LONG_VARIABLE;
        ABORT;
        }
      strcat(pEx->Buffer,pEx->ThisFunction->FunctionName );
      }

    pSymbol = sym_LookupSymbol(pEx->Buffer, /* the symbol we search */
                               pEx->GlobalLabels, /* in this table */
                               1,                 /* insert the symbol as new */
                               alloc_Alloc,
                               alloc_Free,
                               pEx->pSymbolTableMemorySegment);
    ASSERT_NON_NULL(pSymbol)
    if( *pSymbol == NULL ){
      *pSymbol = (void *)new_SymbolLABEL();
      pLabel = (pSymbolLABEL)*pSymbol;
      pLabel->node = 0;
      }else{
      pLabel = (pSymbolLABEL)*pSymbol;
      }
    if( pLabel == NULL )ABORT;
    if( pLabel->node != 0 )*piFailure = EX_ERROR_LABEL_DOUBLE_DEFINED;
    ex_PushWaitingLabel(pEx,pLabel);
    }

  /* if we have just left the local scope (we are not local and we were local) */
  if( (!pEx->iWeAreLocal) && iSaveWeAreLocal ){
    /* free the memory assigned to local variables */
    alloc_FreeSegment(pEx->pLocalVarMemorySegment);
    pEx->LocalVariables = NULL; /* just to be safe */
    pEx->LocallyDeclaredGlobalVariables = NULL; /* also just to be safe */
    pEx->ThisFunction = NULL; /* there is no this function */
    ex_CleanLabelStack(); /* no nested construct cross locality border */
    }

  /* if this was a function definition then the number of local variables give the
     number of arguments because the arguments are nothing else than initialized
     local variables
  *//* we are in a function and the number of arguments was not set => this was the function head */
  if( pEx->ThisFunction && pEx->ThisFunction->Argc == -1 )pEx->ThisFunction->Argc = pEx->cLocalVariables;

  /* If we are not local then this pointer has to be NULL. This may happen when a 'declare sub XX alias "xx" lib "xx"'
     command is parsed. This sets this pointer to point to a function structure, but neither enters nor leaves
     local scope. On the other hand some of the code relies on this pointer.
  */
  if( !pEx->iWeAreLocal )pEx->ThisFunction = NULL;

  if( fResetNameSpace ){
    /* we have to swap the memory segments pMyMemorySegment and pEx->pMemorySegment because this
       function relies on the segment and tries to release the memory allocated to store the name
       of the name space */
    ex_SwapMemorySegment();
    if( *piFailure = expression_PopNameSpace(pEx) ){
      ex_SwapMemorySegment();
      ABORT;
      }
    ex_SwapMemorySegment();
    }
  if( pszNewNameSpace ){
    if( pEx->cbCurrentNameSpace < (long)strlen(pszNewNameSpace)+3 ){
      *piFailure = EX_ERROR_TOO_LONG_NAME_SPACE;
      ABORT;
      }
    if( *piFailure = expression_PushNameSpace(pEx) )ABORT;
    strcpy(pEx->CurrentNameSpace,pszNewNameSpace);
    strcat(pEx->CurrentNameSpace,"::");
    }

  if( pCommandNode == NULL && iCommandNeedsCode ){
    NewArgument;
    pCommandNode->Parameter.CommandArgument.Argument.pNode = NULL; /* just to be safe */
    }

  if( pCommandNode )
    pCommandNode->OpCode = p->CommandOpCode;

  alloc_MergeAndFinish(pEx->pMemorySegment,pMyMemorySegment);

  return pCommandNode;

#undef ABORT
#undef NextArgument
#undef NewArgument
#undef ARGUMENT

SYNTAX_FAILURE_DO_CLEANUP:
  if( ! *piFailure )
    *piFailure = iSideEffectWas ? EX_ERROR_SYNTAX_FATAL : EX_ERROR_SYNTAX; /* general error */

  /* if this started as a function definition but it failed. */
  if( pEx->ThisFunction && pEx->ThisFunction->Argc == -1 ){
    pEx->ThisFunction = NULL;
    /* Here we have to delete the created undefined symbol. Otherwise it would happen that
       the program erroneously reports an undefined function symbol that it thought it
       was going to ge a function defined later, but it turned out not to be a function
       according to the syntax.
    */
    if( pFailedFunctionSymbol )*pFailedFunctionSymbol = NULL;
    }
  pEx->iWeAreLocal = iSaveWeAreLocal; /* it might have changed */

  /* Clean the label stack. This is needed when syntax analysis fails after some
     construct that has already been recognised as a loop/if/... construct. The
     most typical example is

        while expression do     <- "do" is not allowed

     The syntax analizer thinks that this is a "while" command, but fails
     when the keyword "do" is seen. Therefore here we clean the stack.
  */
  while( StackCleanc ){
    ex_PopLabel(NULL);
    StackCleanc--;
    }
  /* drop the memory segment, and */
  alloc_FinishSegment(pEx->pMemorySegment);
  /* restore the old one */
  pEx->pMemorySegment = pMyMemorySegment;
  return NULL;
  }

/*POD
=H ex_Command_r()

This function finds the matching sytax line for the actual line in a loop. It
starts with the first syntax definition and goes on until there are no more
syntax defintions, a fatal error has happened or the actual line is matched.

/*FUNCTION*/
void ex_Command_r(peXobject pEx,
                  peNODE *Result,
                  int *piFailure
  ){
/*noverbatim
T<pEx> is the execution object.

T<Result> is the resulting node.

T<piFailure> is the error code.

CUT*/
  pLineSyntax p;
  pLexeme pPosition;
  int i;
  p = pEx->Command;
  lex_SavePosition(pEx->pLex,&pPosition);
  i = 0;
  while(1){
    i++;
    *Result = p->pfAnalyzeFunction(pEx,p,piFailure);
    if( *piFailure != EX_ERROR_SYNTAX )
      break;
    p++;
    if( p->CommandOpCode == 0 )break;
    lex_RestorePosition(pEx->pLex,&pPosition);
    }
  }

/*POD
=H ex_Command_l()

This function goes over the source lines and performs the syntax analysis. This 
function calls the function R<ex_Command_r()>. When that function returns it
allocated the list nodes that chain up the individual lines. It also defines
the labels that are waiting to be defined.

/*FUNCTION*/
int ex_Command_l(peXobject pEx,
                  peNODE_l *Result
  ){
/*noverbatim
When all the lines are done this function cleans the name space stack,
check for undefined labels that remained undefined still the end 
of the source file.

CUT*/
  int iFailure;
  peNODE pCommand;
  pSymbolLABEL pLabel;
  char *pszFileName;
  long lLineNumber;

  CALL_PREPROCESSOR(PreprocessorExStart,pEx);
  pEx->cLabelsWaiting = 0;
  *Result = NULL;
  while( LexemeType ){
    pszFileName = LexemeFileName;
    lLineNumber = LexemeLineNumber;
    CALL_PREPROCESSOR(PreprocessorExStartLine,pEx);
    ex_Command_r(pEx,&pCommand,&iFailure);
    if( iFailure ){/* this is some error. we go on to the next line */
      REPORT(LexemeFileName,LexemeLineNumber,iFailure,NULL);
      while( LexemeType && (LexemeType != LEX_T_CHARACTER || LexemeChar != '\n') ) NextLexeme;
      continue;
      }
    /* some lines may return a NULL (no code generated, but still success, like "MODULE MyModule" */
    if( pCommand ){/* if there was any code then step onto the next line. */
      *Result = new_eNODE_lL();/* create the code for the line head */
      CALL_PREPROCESSOR(PreprocessorExLineNode,pEx);
      if( *Result == NULL )return EX_ERROR_MEMORY_LOW;
      (*Result)->actualm = pCommand;/* link in the code of the line */

      /* define all the labels that were present on the line or on a previous line and
         still wait for the next code generating line to be assigned to */
      while( pLabel = ex_PopWaitingLabel(pEx) )pLabel->node = (*Result)->NodeId;
      if( pEx->pFunctionWaiting ){
        pEx->pFunctionWaiting->node = (*Result)->NodeId;
        pEx->pFunctionWaiting = NULL;
        }
      Result = &((*Result)->rest); /* step onto the next line pointer (line itself is not yet allocated) */
      *Result = NULL; /* because the line is not allocated and may not be allocated if this was the last line
                         that allocated any node */
      }
    }

  /* if there are labels waiting to be defined after the last command line then we
     create a dummy list node without members or rest */
  if( pLabel = ex_PopWaitingLabel(pEx) ){
    *Result = new_eNODE_lL();
    if( *Result == NULL )return EX_ERROR_MEMORY_LOW;
    (*Result)->actualm = NULL;
    (*Result)->rest = NULL;
    while( pLabel ){
      pLabel->node = (*Result)->NodeId;
      pLabel = ex_PopWaitingLabel(pEx);
      }
    }

  /* This function call cleans the name space stack. This is not really neccessary because the
     name space stack is cleand up by the caller when finishes all the memory segments of the
     lexer and expression module. The main reason calling this function here is that this function
     reports an error if the name space stack is not empty. This effectively means that a "moduile"
     statement was not tfollowed by an "end module" statement until the end of the file. */
  ex_CleanNameSpaceStack(pEx);

  /* there is not really a need to clean the label stack, but this function also
     calls error reports. This will generate error reports for programs that end
     before closing a 'while' 'if' or any other loop like statement */
  ex_CleanLabelStack();

  /* this function goes through the symbol table where labels are defined and error reports
     all labels that were used, but not defined.
  */
  ex_CheckUndefinedLabels(pEx);

  /* This is a little hack. All other part of this program assumes that there are global
     variables. However a "Hello Word" application may not have and it causes
     trouble, when allocation functions allocate zero size memory. Therefore we create
     at least one global variable.  */
  if( pEx->cGlobalVariables == 0 )pEx->cGlobalVariables++;
  CALL_PREPROCESSOR(PreprocessorExEnd,pEx);
  return 0;
  }

/*FUNCTION*/
void ex_pprint(FILE *f,
               peXobject pEx
  ){
  peNODE_l q;

  for( q = pEx->pCommandList ; q ; q = q->rest )
    _ex_pprint(f,q->actualm,pEx,0);
  }


#define ABORT  do{ *piFailure = EX_ERROR_SYNTAX;return NULL; }while(0)
#define FABORT do{ *piFailure = EX_ERROR_SYNTAX_FATAL;return NULL; }while(0)
#define ARGUMENT pArgument->Parameter.CommandArgument.Argument
#define ASSERT_NON_NULL(x) if( (x) == NULL ){ *piFailure = EX_ERROR_MEMORY_LOW; ABORT; }

#define NewArgument if( (*ppArgument = new_eNODE()) == NULL ){\
                       *piFailure = EX_ERROR_MEMORY_LOW;\
                       return NULL;\
                       }else{\
                       pArgument = *ppArgument;\
                       ppArgument = &(pArgument->Parameter.CommandArgument.next);\
                       *ppArgument = NULL;\
                       }

/*POD
=H ex_Pragma

This function implements the compiler directive "declare option".

When the compiler finds a "declare option" directive it calls this function.
The first argument is the compiler class pointer. The second argument points
to a constant string containing the option.

The function implements the internal settings of the compiler options reflecting
the programmer needs expressed by the option. For example DeclareVars will
require all variables declared to be either global or local.

If the programmer specified an option, which is not implemented the error reporting
function is called.

/*FUNCTION*/
int ex_Pragma(peXobject pEx,
              char *pszPragma
  ){
/*noverbatim

The function returns T<0> when the option was processed, and T<1> when not implemented
option was supplied as argument.
CUT*/

  if( ! strcmp(pszPragma,"DeclareVars") ){
    DeclareVars = 1;
    return 0;
    }

  if( ! strcmp(pszPragma,"AutoVars") ){
    DeclareVars = 0;
    return 0;
    }

  if( ! strcmp(pszPragma,"DefaultLocal") ){
    DefaultLocal = 1;
    return 0;
    }

  if( ! strcmp(pszPragma,"DefaultGlobal") ){
    DefaultLocal = 0;
    return 0;
    }

  REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_UNDEF_PRAGMA,pszPragma);
  return 1;
  }

/*POD
=H ex_IsCommandCALL()

Because the syntax of a call statement is very special here is a special
function to analyze the CALL statement.

A call statement is a keyword CALL followed by a function call.

If the function or sub is already defined then the keyword CALL can be missing.

When the function or sub is called this way and not inseide an expression the
enclosing parentheses can be missing.

/*FUNCTION*/
peNODE ex_IsCommandCALL(peXobject pEx,
                        pLineSyntax p,
                        int *piFailure
  ){
/*noverbatim

To get some description of waiting labels see the description of the function R<ex_PushWaitingLabel()>.

CUT*/

  peNODE pCommandNode,q;
  peNODE *ppArgument,pArgument;
  char *pszFN;
  int iOpened; /* flag to check that there was an opening ( for the arguments */
  int PCbeforeNL; /* flag to check that a ) was immediately before the end of the line */
  void **pSymbol;
  pLexeme pPosition;

  *piFailure = EX_ERROR_SUCCESS;

  ppArgument = &pCommandNode;
  pCommandNode = NULL;

  pSymbol = NULL;
  if( LexemeType == LEX_T_NSYMBOL && LexemeCode == p->lexes[0].OpCode ){
    NextLexeme;
    }else{
    if( LexemeType == LEX_T_ASYMBOL ){
      ex_ConvertName(pszFN=LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
      pSymbol = ex_LookupUserFunction(pEx,0);
      if( pSymbol == NULL )ABORT;
      }else ABORT;

    }

  /* the function name should come */
  if( LexemeType != LEX_T_ASYMBOL )ABORT;


  if( pSymbol == NULL ){/* pSymbol is NULL here if there was a CALL keyword on the line
                           otherwise we have already checked that this symbol was defined
                           as a sub or function */
    ex_ConvertName(pszFN=LexemeSymbol, pEx->Buffer,pEx->cbBuffer,pEx);
    pSymbol = ex_LookupUserFunction(pEx,1);
    }
  if( *pSymbol == NULL ){
    /* This function has not been defined. */
    *pSymbol = (void *)new_SymbolUF();
    if( *pSymbol == NULL )return NULL;
    ((pSymbolUF)*pSymbol)->FunctionName = pszFN;
    }

  NextLexeme;
  if( LexemeType == LEX_T_NSYMBOL && LexemeCode == CMD_EQ )ABORT; /* this seems to be an assignment */
  if( LexemeType == LEX_T_CHARACTER && LexemeChar == '[' )ABORT; /* this seems to be an assignment to an array element */

  /* now we can be sure that this is either a call statement or syntax error */
  NewArgument;
  q = new_eNODE();
  if( q == NULL ){
    *piFailure = EX_ERROR_MEMORY_LOW;
    return NULL;
    }
  ARGUMENT.pNode = q;
  q->OpCode = eNTYPE_FUN;
  q->Parameter.UserFunction.pFunction = (pSymbolUF)(*pSymbol);

  if( LexemeType == LEX_T_CHARACTER && LexemeChar == '(' ){
    /* Think about the following subroutine call:

       gotoxy (i+2)/3 , (j+2)/4

       Does it have the form [gotoxy a,b] or [gotoxy(a,b)].
       Obviously it is the first, but the expression list starts with a
       '(' character that confused the syntax analyzer before v1.0b24.
       This piece of code checks that if the expression list starts
       with a '(' character that there is or not a ',' out of all
       '(' ')' pairs. If there is then the starting '(' starts the first
       expression. If not then the '(' starts the expression list.

       The variable iOpened is used to count up and down the opening and
       closing parentheses. Finally it contains the number of net opening
       parentheses till the first comma or zero in case there is no comma
       on the line.
     */
    lex_SavePosition(pEx->pLex,&pPosition);
    iOpened = 0;
    while( ! lex_EOF(pEx->pLex) && !(LexemeType == LEX_T_CHARACTER && LexemeChar == '\n') ){
      PCbeforeNL = 0;
      if( LexemeType == LEX_T_CHARACTER && LexemeChar == '(' )iOpened++;
      else
      if( LexemeType == LEX_T_CHARACTER && LexemeChar == ')' ){
        iOpened--;
        PCbeforeNL = 1;
        }
      else
      if( LexemeType == LEX_T_CHARACTER && LexemeChar == ',' && iOpened == 1 ){
        goto CommaFound;
        }
      NextLexeme;
      }
    /* no comma was found on the command line*/
    iOpened = PCbeforeNL;
CommaFound:
    lex_RestorePosition(pEx->pLex,&pPosition);
    if( iOpened )
      NextLexeme;
    }else iOpened = 0;

  if( iOpened && LexemeType == LEX_T_CHARACTER && LexemeChar == ')' ){
    /* empty parameter list */
    q->Parameter.UserFunction.Argument = NULL;
    NextLexeme;
    if( LexemeType == LEX_T_CHARACTER && LexemeChar == '\n' ){
       NextLexeme;
       pCommandNode->OpCode = p->CommandOpCode;
       return pCommandNode;
       }else FABORT;
     }

  if( (! iOpened) && LexemeType == LEX_T_CHARACTER && LexemeChar == '\n' ){
    q->Parameter.UserFunction.Argument = NULL;
    NextLexeme;
    pCommandNode->OpCode = p->CommandOpCode;
    return pCommandNode;
    }

  q->Parameter.UserFunction.Argument = ex_ExpressionList(pEx);
  if( q->Parameter.UserFunction.Argument == NULL )FABORT;
  if( iOpened ){
    if( LexemeType != LEX_T_CHARACTER || LexemeChar != ')' ){
      /* the closing ) is missing after function call */
      REPORT(LexemeFileName,LexemeLineNumber,EX_ERROR_MISSING_PAREN,NULL);
      FABORT;
      }else{ NextLexeme; }
    }

  /* check that there is nothing else on the line */
  if( LexemeType != LEX_T_CHARACTER || LexemeChar != '\n' )FABORT;

  pCommandNode->OpCode = p->CommandOpCode;
  return pCommandNode;
  }

/*POD
=H ex_IsCommandOPEN()

The open statement is a simple one. The only problem is that the last parameter
defining the length of a record is optional. This can only be handled using a separate
function

/*FUNCTION*/
peNODE ex_IsCommandOPEN(peXobject pEx,
                        pLineSyntax p,
                        int *piFailure
  ){
/*noverbatim

'open' expression 'for' absolute_symbol 'as' expression 'len' '=' expression nl

CUT*/
  peNODE pCommandNode,q;
  peNODE *ppArgument,pArgument;
  long sLen;

  *piFailure = EX_ERROR_SUCCESS;

  ppArgument = &pCommandNode;
  pCommandNode = NULL;

  /* check the keyword OPEN */
  if( LexemeType != EX_LEX_NSYMBOL || LexemeCode != p->lexes[0].OpCode )ABORT;
  NextLexeme;

  /* file name */
  NewArgument;
  ex_Expression_r(pEx,&(ARGUMENT.pNode));
  if( ARGUMENT.pNode == NULL )FABORT;

  /* keyword FOR */
  if( LexemeType != EX_LEX_NSYMBOL || LexemeCode != p->lexes[2].OpCode )FABORT;
  NextLexeme;

  /* FOR what? */
  if( LexemeType != LEX_T_ASYMBOL && LexemeType != LEX_T_NSYMBOL )FABORT;
  if( LexemeType == LEX_T_ASYMBOL ){
    NewArgument;
    if( (ARGUMENT.szStringValue = alloc_Alloc((sLen=strlen(LexemeSymbol))+1,pEx->pMemorySegment)) == NULL )ABORT;
    strcpy(ARGUMENT.szStringValue,LexemeSymbol);
    pArgument->Parameter.CommandArgument.sLen = sLen;
    pEx->cbStringTable += sLen+1;
    COUNT_STRING_LEN
    }else{/* this is to handle absolute symbols that are predefined */
    NewArgument;
    if( (ARGUMENT.szStringValue = alloc_Alloc((sLen=strlen(lex_SymbolicName(pEx->pLex,LexemeCode)))+1,pEx->pMemorySegment)) == NULL )ABORT;
    strcpy(ARGUMENT.szStringValue,lex_SymbolicName(pEx->pLex,LexemeCode));
    pArgument->Parameter.CommandArgument.sLen = sLen;
    pEx->cbStringTable += strlen(ARGUMENT.szStringValue)+1;
    COUNT_STRING_LEN
    }
  NextLexeme;

  /* AS */
  if( LexemeType != EX_LEX_NSYMBOL || LexemeCode != p->lexes[4].OpCode )FABORT;
  NextLexeme;
  /* optional # after the keyword AS */
  if( LexemeType == LEX_T_CHARACTER && LexemeCode == '#' )
    NextLexeme;

  /* file number expression */
  NewArgument;
  ex_Expression_r(pEx,&(ARGUMENT.pNode));
  if( ARGUMENT.pNode == NULL )FABORT;

  /* LEN */
  if( LexemeType == LEX_T_NSYMBOL && LexemeCode == p->lexes[6].OpCode ){
    NextLexeme;
    if( LexemeType != LEX_T_NSYMBOL || LexemeCode != p->lexes[7].OpCode )FABORT;
    NextLexeme;

    /* record length */
    NewArgument;
    ex_Expression_r(pEx,&(ARGUMENT.pNode));
    if( ARGUMENT.pNode == NULL )FABORT;
    }else{
    /* no len expression, default expression constant 1 */
    NewArgument;
    q = new_eNODE();
    if( q == NULL ){
      *piFailure = EX_ERROR_MEMORY_LOW;
      return NULL;
      }
    ARGUMENT.pNode = q;
    q->OpCode = eNTYPE_LNG;
    q->Parameter.Constant.Value.lValue = 1;
    }

  /* check the new line at the end of the line */
  if( LexemeType != LEX_T_CHARACTER || LexemeCode != p->lexes[9].OpCode )FABORT;
  NextLexeme;

  pCommandNode->OpCode = p->CommandOpCode;
  return pCommandNode;
  }

/*POD
=H ex_IsCommandSLIF()

If syntax analysis gets to calling this function the command is surely
not single line if, because the command SLIF is recognised by T<IsCommandIF>.

The syntax of the command IF is presented in the syntax table before
SLIF and therefore if the syntax analyser gets here it can not be
SLIF.

The original function T<IsCommandThis> could also do failing automatically,
but it is simpler just to fail after the function call, so this function
is just a bit of speedup.
/*FUNCTION*/
peNODE ex_IsCommandSLIF(peXobject pEx,
                        pLineSyntax p,
                        int *piFailure
  ){
/*noverbatim
CUT*/
  ABORT;
  }

/*POD
=H ex_IsCommandIF()

The statement IF is quite simple. However there is another
command that has almost the same syntax as the IF statement.
This is the SLIF, single line IF.

The difference between the command IF and SLIF is that SLIF does
not have the new line character after the keyword T<THEN>.

/*FUNCTION*/
peNODE ex_IsCommandIF(peXobject pEx,
                        pLineSyntax p,
                        int *piFailure
  ){
/*noverbatim

IF/IF:    'if' * expression 'then' go_forward(IF) nl
SLIF/SLIF:  'slif' * expression 'then'


CUT*/
  peNODE pCommandNode;
  peNODE *ppArgument,pArgument;
  pSymbolLABEL pLabel;

  *piFailure = EX_ERROR_SUCCESS;

  ppArgument = &pCommandNode;
  pCommandNode = NULL;

  /* check the keyword IF */
  if( LexemeType != EX_LEX_NSYMBOL || LexemeCode != p->lexes[0].OpCode )ABORT;
  NextLexeme;

  /* expression */
  NewArgument;
  ex_Expression_r(pEx,&(ARGUMENT.pNode));
  if( ARGUMENT.pNode == NULL )FABORT;

  /* keyword THEN */
  if( LexemeType != EX_LEX_NSYMBOL || LexemeCode != p->lexes[3].OpCode )FABORT;
  NextLexeme;

  /* check the new line at the end of the line */
  if( LexemeType == LEX_T_CHARACTER && LexemeCode == p->lexes[5].OpCode ){
    /* this is an IF statement */
    pCommandNode->OpCode = p->CommandOpCode;  
    NextLexeme;
    NewArgument;
    pLabel = (ARGUMENT.pLabel = new_SymbolLABEL());
    _ex_PushLabel(pEx,pLabel,CMD_IF,pEx->pMemorySegment);
    }else{
    /* this is a SLIF statement */
    /* There are some restrictions on the commands that may not follow
       the SLIF command. These are: */
    if( LexemeType == EX_LEX_NSYMBOL &&
        ( LexemeCode == KEYWORDCODE_IF      || /* another IF statement    */
          LexemeCode == KEYWORDCODE_DECLARE || /* declare sub statement   */
          LexemeCode == KEYWORDCODE_MODULE  || /* module statement        */
          LexemeCode == KEYWORDCODE_ELSEIF  || /* else if statement       */
          LexemeCode == KEYWORDCODE_ELSIF   || /* else if statement       */
          LexemeCode == KEYWORDCODE_ELIF    || /* else if statement       */
          LexemeCode == KEYWORDCODE_ELSE    || /* else statement          */
          LexemeCode == KEYWORDCODE_ENDIF   || /* endif statement         */

          LexemeCode == KEYWORDCODE_GLOBAL  || /* global const            */
          LexemeCode == KEYWORDCODE_CONST   || /* const declaration       */
          LexemeCode == KEYWORDCODE_LOCAL   || /* local variable          */
          LexemeCode == KEYWORDCODE_VAR     || /* variable declaration    */

          LexemeCode == KEYWORDCODE_REPEAT  || /* any loop construct start*/
          LexemeCode == KEYWORDCODE_UNTIL   || /*                         */
          LexemeCode == KEYWORDCODE_FOR     || /*                         */
          LexemeCode == KEYWORDCODE_WHILE   || /*        or               */
          LexemeCode == KEYWORDCODE_NEXT    || /*                         */
          LexemeCode == KEYWORDCODE_WEND    || /*   end                   */

          LexemeCode == KEYWORDCODE_SUB     || /* subroutine start        */
          LexemeCode == KEYWORDCODE_FUNCTION|| /* function start          */
          LexemeCode == KEYWORDCODE_END     || /* end if/sub/function etc */
          0
        ) )FABORT;
    pCommandNode->OpCode = CMD_SLIF;
    }

  return pCommandNode;
  }

/*POD
=H ex_IsCommandLET()

/*FUNCTION*/
peNODE ex_IsCommandLET(peXobject pEx,
                       pLineSyntax p,
                       int *piFailure
  ){
/*noverbatim

CUT*/
  peNODE pCommandNode;
  peNODE *ppArgument,pArgument;
  
  *piFailure = EX_ERROR_SUCCESS;

  ppArgument = &pCommandNode;
  pCommandNode = NULL;

  if( LexemeType == EX_LEX_NSYMBOL && LexemeCode == KEYWORDCODE_LET )NextLexeme;

  if( LexemeType != LEX_T_ASYMBOL )ABORT; /* this is needed only to ease a bit 
                                              syntax defintion table build up. */
  NewArgument;
  if( (ARGUMENT.pNode = ex_LeftValue(pEx)) == NULL )FABORT;

  if(  LexemeType != EX_LEX_NSYMBOL )FABORT;
  switch( LexemeCode ){
    case CMD_EQ:
      pCommandNode->OpCode = CMD_LET;  
      break;
    case CMD_EXTOPAG:
      pCommandNode->OpCode = CMD_LETP;
      break;
    case CMD_EXTOPMG:
      pCommandNode->OpCode = CMD_LETC;
      break;
    case CMD_EXTOPHG:
      pCommandNode->OpCode = CMD_LETS;
      break;
    case CMD_EXTOPIG:
      pCommandNode->OpCode = CMD_LETD;
      break;
    case CMD_EXTOPBG:
      pCommandNode->OpCode = CMD_LETM;
      break;
    case CMD_EXTOPNG:
      pCommandNode->OpCode = CMD_LETI;
      break;
    default: FABORT;
    }
  NextLexeme;

  /* expression */
  NewArgument;
  ex_Expression_r(pEx,&(ARGUMENT.pNode));
  if( ARGUMENT.pNode == NULL )FABORT;

  /* check the new line at the end of the line */
  if( LexemeType == LEX_T_CHARACTER && LexemeCode == '\n' )
    return pCommandNode;
  FABORT;
  }
