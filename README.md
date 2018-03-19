public Integer eval(String exp, int start) {
        Stack<Character>  rator = new Stack<>();
        Stack<Integer>  rands = new Stack<>();
        for (int i = start; i < exp.length(); i++) {

            if (exp.charAt(i) == '+' || exp.charAt(i) == '-' || exp.charAt(i) == '*' || exp.charAt(i) == '/')  {
                rator.push(exp.charAt(i));
            } else if (exp.charAt(i) == ' ') {
                continue;
            } else if (Character.isDigit(exp.charAt(i))) {
                String num = "";
                while(Character.isDigit(exp.charAt(i))) {
                    num += exp.charAt(i);
                    i++;
                }
                rands.push(Integer.valueOf(num));
                i--;
            } else if (exp.charAt(i) == '(') {
                eval(exp, i+1);
            } else if (exp.charAt(i) == ')') {
                int expn = 0;
                while (!rator.isEmpty()) {
                    Character op = rator.pop();
                    Integer rand1 = rands.pop();
                    Integer rand2 = rands.pop();
                    rands.push(fun(op, rand1, rand2));
                }
            }
        }
        return Integer.valueOf(rands.pop());

    }
