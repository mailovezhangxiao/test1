int b = 60;
int k;

int fun1(int x, int y, float z)
{
	switch(y)
	{
		case '+':  	b += 2; break;
		case '-':	x -= 3; break;
		default:	k = y<<1;
	}

	return b + x;
}

int fun2(int x, int y, float z)
{
	if(x < y)
		return 256;
	else
		return k;
}
