float f = -60.6875;
int s[2];
int a = 268;
extern int b;

/* function declaration */
extern int fun1(int x, int y, float z);
extern int fun2(int x, int y, float z);

/* main function */
int main(void)
{
	int i, d = 326;

	s[0] = 20;
	s[1] = a;

	for(i = 1; i < b; i++)
		a += i;

	d = fun2(d, a, f);
	d += -20;
	return d;
}
