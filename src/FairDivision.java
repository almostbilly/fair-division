
public class FairDivision {
    public static String answer = "";

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        double eps = Double.parseDouble(args[1]);

        startDivision(k, eps);
    }

    public static void startDivision(int k, double eps) {

        double valuesA[] = new double[k];
        double valuesB[] = new double[k];
        double valuesC[] = new double[k];

        double sumValuesC = 0;

        for (int i = 0 ; i < k; i++) {
            valuesA[i] = 1 - Math.random();
            valuesB[i] = 1 - Math.random();
            valuesC[i] = 1 - Math.random();
            sumValuesC += valuesC[i];
        }
        double partC = sumValuesC / 3.0;

        // определяем метки C1 и C2, делящие k объектов на три равные части по критерию C.
        int markC1 = 0;
        double C[] = new double[3];
        for (int i = 0; i < 3; i++) {
            C[i] = 0;
        }

        while (C[0] + valuesC[markC1] < partC) {
            C[0] += valuesC[markC1];
            markC1++;
        }

        double percentMarkC1 = (((int)((partC - (C[0] % partC)) / eps) * eps) / valuesC[markC1]);
        C[1] += (1 - percentMarkC1) * valuesC[markC1];
        C[0] += percentMarkC1 * valuesC[markC1];

        int markC2 = markC1;
        if (C[1] < partC)
            markC2++;

        while (C[1] + valuesC[markC2] < partC) {
            C[1] += valuesC[markC2];
            markC2++;
        }
        double percentMarkC2 = (((int)((partC - (C[1] % partC)) / eps) * eps) / valuesC[markC2]);
        C[1] += percentMarkC2 * valuesC[markC2];

        C[2] = sumValuesC - C[0] - C[1];

        // ценность частей, соответствующих разным критериям.
        double A[] = new double[3];
        double B[] = new double[3];
        for (int i = 0; i < 3; i++) {
            A[i] = 0;
            B[i] = 0;
        }

        // считаем ценности первых частей A и B.
        for (int i = 0; i < markC1; i++) {
            A[0] += valuesA[i];
            B[0] += valuesB[i];
        }
        A[0] += percentMarkC1 * valuesA[markC1];
        B[0] += percentMarkC1 * valuesB[markC1];

        // считаем ценности вторых частей A и B.
        for (int i = markC1; i < markC2; i++) {
            A[1] += valuesA[i];
            B[1] += valuesB[i];
        }
        A[1] -= percentMarkC1 * valuesA[markC1];
        A[1] += percentMarkC1 * valuesA[markC1];
        B[1] -= percentMarkC2 * valuesB[markC2];
        B[1] += percentMarkC2 * valuesB[markC2];

        // считаем ценности третьих частей A и B.
        for (int i = markC2; i < k; i++) {
            A[2] += valuesA[i];
            B[2] += valuesB[i];
        }
        A[2] -= percentMarkC2 * valuesA[markC2];
        B[2] -= percentMarkC2 * valuesB[markC2];


        // определяем самые ценные части по соотвествующим критериям оценивания.
        double mostValueA = 0, mostValueB = 0, secondValueA = 0, secondValueB = 0;
        int numMostValueA = 0, numMostValueB = 0, numSecondValueA = 0, numSecondValueB = 0;

        for (int i = 0; i < 3; i++) {
            if (A[i] > mostValueA) {
                secondValueA = mostValueA;
                numSecondValueA = numMostValueA;
                mostValueA = A[i];
                numMostValueA = i;
            }
            else if (A[i] > secondValueA) {
                secondValueA = A[i];
                numSecondValueA = i;
            }

            if (B[i] > mostValueB) {
                secondValueB = mostValueB;
                numSecondValueB = numMostValueB;
                mostValueB = B[i];
                numMostValueB = i;
            }
            else if (B[i] > secondValueB) {
                    secondValueB = B[i];
                    numSecondValueB = i;
            }
        }

        if (numMostValueA != numMostValueB) {
            int numC = 0;
            while (numC == numMostValueA || numC == numMostValueB)
                numC++;
            System.out.println("Все просто");
            System.out.println("A выбирает " + (numMostValueA + 1) + " часть");
            System.out.println("B выбирает " + (numMostValueB + 1) + " часть");
            System.out.println("C выбирает " + (numC + 1) + " часть");
            System.out.print("По критерию A: ");
            for (int i = 0; i < 3; i++) {
                System.out.print(A[i] + " ");
            }
            System.out.println();

            System.out.print("По критерию B: ");
            for (int i = 0; i < 3; i++) {
                System.out.print(B[i] + " ");
            }
            System.out.println();

            System.out.print("По критерию C: ");
            for (int i = 0; i < 3; i++) {
                System.out.print(C[i] + " ");
            }
            System.out.println();
        }
        else {
            // ценности остатка
            double trimA[] = new double[3];
            double trimB[] = new double[3];
            double trimC[] = new double[3];
            for (int i =0; i < 3; i++) {
                trimA[i] = 0;
                trimB[i] = 0;
                trimC[i] = 0;
            }

            // ценность отсеченного куска по критерию B
            double sumB = 0;
            // markB1 - метка разделения mostValueB части, таким образом, что отсеченный кусок равен secondValueB
            // также является началом trim (остатка)
            int markB1, endTrim;
            switch (numMostValueB) {
                case 0:
                    markB1 = 0;
                    endTrim = markC1;
                    break;
                case 1:
                    markB1 = markC1;
                    endTrim = markC2;
                    break;
                default:
                    markB1 = markC2;
                    endTrim = k;
                    break;
            }

            // находим метку отсеченного куска
            while (sumB + valuesB[markB1] < secondValueB) {
                sumB += valuesB[markB1];
                markB1++;
            }
            double percentMarkB1 = (((int)((secondValueB - (sumB % secondValueB)) / eps) * eps) / valuesB[markB1]);

            // определяем ценность остатка по критериям A и B
            double valueTrimA = 0, valueTrimB = 0, valueTrimC = 0;
            for (int i = markB1 + 1; i < endTrim; i++) {
                valueTrimA += valuesA[i];
                valueTrimB += valuesB[i];
                valueTrimC += valuesC[i];
            }
            valueTrimA += (1 - percentMarkB1) * valuesA[markB1];
            valueTrimB += (1 - percentMarkB1) * valuesB[markB1];
            valueTrimC += (1 - percentMarkB1) * valuesC[markB1];

            // определяем ценность отсеченного куска
            A[numMostValueA] -= valueTrimA;
            B[numMostValueA] -= valueTrimB;
            C[numMostValueA] -= valueTrimC;

            // Вывод основной части
            answer += "Основная часть:\n";
            // определяем самую ценную часть по критерию A среди отсеченного и остальных частей
            // A выбирает отсеченный кусок, B выбирает второй по ценности, C достается последний
            if (A[numMostValueA] > secondValueA) {
                mostValueA = A[numMostValueA];

                int numC = 0;
                while (numC == numMostValueA || numC == numSecondValueB) {
                    numC++;
                }

                answer += "A выбрал " + (numMostValueA + 1) + " часть\n";
                answer += "B выбрал " + (numSecondValueB + 1) + " часть\n";
                answer += "C выбрал " + (numC + 1) + " часть\n";
                answer += "Ценности:\n";
                answer += "A: ";
                for (int i = 0; i < 3; i++) {
                    answer += A[i] + " ";
                }
                answer += "\n";
                answer += "B: ";
                for (int i = 0; i < 3; i++) {
                    answer += B[i] + " ";
                }
                answer += "\n";
                answer += "C: ";
                for (int i = 0; i < 3; i++) {
                    answer += C[i] + " ";
                }
                answer += "\n";
                System.out.println(answer);
                answer = "";

                // делим остаток
                // дележ остатка совершает B
                double partTrimB = valueTrimB / 3.0;

                int markTrimB1 = markB1;
                while (trimB[0] + valuesB[markTrimB1] < partTrimB) {
                    trimB[0] += valuesB[markTrimB1];
                    markTrimB1++;
                }
                double percentMarkTrimB1 = (((int)((partTrimB - (trimB[0] % partTrimB)) / eps) * eps) / valuesB[markTrimB1]);
                trimB[1] += (1 - percentMarkTrimB1) * valuesB[markTrimB1];
                trimB[0] += percentMarkTrimB1 * valuesB[markTrimB1];

                int markTrimB2 = markTrimB1;
                if (trimB[1] < partTrimB)
                    markTrimB2++;

                while (trimB[1] + valuesB[markTrimB2] < partTrimB) {
                    trimB[1] += valuesB[markTrimB2];
                    markTrimB2++;
                }
                double percentMarkTrimB2 = (((int)((partTrimB - (trimB[1] % partTrimB)) / eps) * eps) / valuesB[markTrimB2]);
                trimB[1] += percentMarkTrimB2 * valuesB[markTrimB2];

                trimB[2] = valueTrimB - trimB[0] - trimB[1];

                // определяем ценность частей остатка для критериев A и C
                for (int i = markB1; i < markTrimB1; i++) {
                    trimC[0] += valuesC[i];
                    trimA[0] += valuesA[i];
                }
                for (int i = markTrimB1; i < markTrimB2; i++) {
                    trimC[1] += valuesC[i];
                    trimA[1] += valuesA[i];
                }
                for (int i = markTrimB2; i < endTrim; i++) {
                    trimC[2] += valuesC[i];
                    trimA[2] += valuesA[i];
                }

                double mostTrimA = 0;
                int numMostTrimA = 0;
                for (int i = 0; i < 3; i++) {
                    if (trimA[i] > mostTrimA) {
                        mostTrimA = trimA[i];
                        numMostTrimA = i;
                    }
                }
                double mostTrimC = 0, secondTrimC = 0;
                int numMostTrimC = 0, numSecondTrimC = 0;
                for (int i = 0; i < 3; i++) {
                    if (trimC[i] > mostTrimC) {
                        secondTrimC = mostTrimC;
                        numSecondTrimC = numMostTrimC;
                        mostTrimC = trimC[i];
                        numMostTrimC = i;
                    }
                    else if (trimC[i] > secondTrimC) {
                        secondTrimC = trimC[i];
                        numSecondTrimC = i;
                    }
                }

                int numB = 0;
                answer += "Остаток:\n";
                // A забирает most, а C забирает second
                if (numMostTrimA == numMostTrimC) {
                    while (numB == numMostTrimA || numB == numSecondTrimC)
                        numB++;
                    answer += "A выбрал " + (numMostTrimA + 1) + " часть\n";
                    answer += "C выбрал " + (numSecondTrimC + 1) + " часть\n";
                    answer += "B выбрал " + (numB + 1) + " часть\n";
                    answer += "Ценности остатка:\n";
                    answer += "A: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimA[i] + " ";
                    }
                    answer += "\n";
                    answer += "C: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimC[i] + " ";
                    }
                    answer += "\n";
                    answer += "B: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimB[i] + " ";
                    }
                    answer += "\n";
                    System.out.println(answer);
                    answer = "";

                    answer += "Суммарные ценности:\n";
                    answer += "По критерию A: A - " + (A[numMostValueA] + trimA[numMostTrimA]) + " B - " + (A[numSecondValueB] + trimA[numB]) + " C - " + (A[numC] + trimA[numSecondTrimC]) + "\n";
                    answer += "По критерию B: A - " + (B[numMostValueA] + trimB[numMostTrimA]) + " B - " + (B[numSecondValueB] + trimB[numB]) + " C - " + (B[numC] + trimB[numSecondTrimC]) + "\n";
                    answer += "По критерию C: A - " + (C[numMostValueA] + trimC[numMostTrimA]) + " B - " + (C[numSecondValueB] + trimC[numB]) + " C - " + (C[numC] + trimC[numSecondTrimC]) + "\n";
                    System.out.println(answer);
                    answer = "";
                }
                // иначе все просто
                else {
                    while (numB == numMostTrimA || numB == numMostTrimC)
                        numB++;
                    answer += "A выбрал " + (numMostTrimA + 1) + " часть\n";
                    answer += "C выбрал " + (numMostTrimC + 1) + " часть\n";
                    answer += "B выбрал " + (numB + 1) + " часть\n";
                    answer += "Ценности остатка:\n";
                    answer += "A: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimA[i] + " ";
                    }
                    answer += "\n";
                    answer += "C: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimC[i] + " ";
                    }
                    answer += "\n";
                    answer += "B: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimB[i] + " ";
                    }
                    answer += "\n";
                    System.out.println(answer);
                    answer = "";

                    answer += "Суммарные ценности:\n";
                    answer += "По критерию A: A - " + (A[numMostValueA] + trimA[numMostTrimA]) + " B - " + (A[numSecondValueB] + trimA[numB]) + " C - " + (A[numC] + trimA[numMostTrimC]) + "\n";
                    answer += "По критерию B: A - " + (B[numMostValueA] + trimB[numMostTrimA]) + " B - " + (B[numSecondValueB] + trimB[numB]) + " C - " + (B[numC] + trimB[numMostTrimC]) + "\n";
                    answer += "По критерию C: A - " + (C[numMostValueA] + trimC[numMostTrimA]) + " B - " + (C[numSecondValueB] + trimC[numB]) + " C - " + (C[numC] + trimC[numMostTrimC]) + "\n";
                    System.out.println(answer);
                    answer = "";
                }
            }
            // A не выбирает отсеченный кусок, С выбирает вторым
            else {
                int numC = 0;
                while (numC == numMostValueA || numC == numSecondValueA) {
                    numC++;
                }

                A[numMostValueA] = secondValueA;
                numMostValueA = numSecondValueA;

                answer += "A выбрал " + (numMostValueA + 1) + " часть\n";
                answer += "C выбрал " + (numC + 1) + " часть\n";
                answer += "B выбрал " + (numMostValueB + 1) + " часть\n";
                answer += "Ценности:\n";
                answer += "A: ";
                for (int i = 0; i < 3; i++) {
                    answer += A[i] + " ";
                }
                answer += "\n";
                answer += "B: ";
                for (int i = 0; i < 3; i++) {
                    answer += B[i] + " ";
                }
                answer += "\n";
                answer += "C: ";
                for (int i = 0; i < 3; i++) {
                    answer += C[i] + " ";
                }
                answer += "\n";
                System.out.println(answer);
                answer = "";

                // делим остаток
                // дележ остатка совершает A
                double partTrimA = valueTrimA / 3.0;

                int markTrimA1 = markB1;
                while (trimA[0] + valuesA[markTrimA1] < partTrimA) {
                    trimA[0] += valuesA[markTrimA1];
                    markTrimA1++;
                }
                double percentMarkTrimA1 = (((int)((partTrimA - (trimA[0] % partTrimA)) / eps) * eps) / valuesA[markTrimA1]);
                trimA[1] += (1 - percentMarkTrimA1) * valuesA[markTrimA1];
                trimA[0] += percentMarkTrimA1 * valuesA[markTrimA1];

                int markTrimA2 = markTrimA1;
                if (trimA[1] < partTrimA)
                    markTrimA2++;

                while (trimA[1] + valuesA[markTrimA2] < partTrimA) {
                    trimA[1] += valuesA[markTrimA2];
                    markTrimA2++;
                }
                double percentMarkTrimA2 = (((int)((partTrimA - (trimA[1] % partTrimA)) / eps) * eps) / valuesA[markTrimA2]);
                trimA[1] += percentMarkTrimA2 * valuesA[markTrimA2];

                trimA[2] = valueTrimA - trimA[0] - trimA[1];

                // определяем ценность частей остатка для критериев A и C
                for (int i = markB1; i < markTrimA1; i++) {
                    trimC[0] += valuesC[i];
                    trimB[0] += valuesB[i];
                }
                for (int i = markTrimA1; i < markTrimA2; i++) {
                    trimC[1] += valuesC[i];
                    trimB[1] += valuesB[i];
                }
                for (int i = markTrimA2; i < endTrim; i++) {
                    trimC[2] += valuesC[i];
                    trimB[2] += valuesB[i];
                }

                double mostTrimB = 0;
                int numMostTrimB = 0;
                for (int i = 0; i < 3; i++) {
                    if (trimB[i] > mostTrimB) {
                        mostTrimB = trimB[i];
                        numMostTrimB = i;
                    }
                }
                double mostTrimC = 0, secondTrimC = 0;
                int numMostTrimC = 0, numSecondTrimC = 0;
                for (int i = 0; i < 3; i++) {
                    if (trimC[i] > mostTrimC) {
                        secondTrimC = mostTrimC;
                        numSecondTrimC = numMostTrimC;
                        mostTrimC = trimC[i];
                        numMostTrimC = i;
                    }
                    else if (trimC[i] > secondTrimC) {
                        secondTrimC = trimC[i];
                        numSecondTrimC = i;
                    }
                }

                int numA = 0;
                answer += "Остаток:\n";
                // A забирает most, а C забирает second
                if (numMostTrimB == numMostTrimC) {
                    while (numA == numMostTrimB || numA == numSecondTrimC)
                        numA++;
                    answer += "B выбрал " + (numMostTrimB + 1) + " часть\n";
                    answer += "C выбрал " + (numSecondTrimC + 1) + " часть\n";
                    answer += "A выбрал " + (numA + 1) + " часть\n";
                    answer += "Ценности остатка:\n";
                    answer += "B: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimB[i] + " ";
                    }
                    answer += "\n";
                    answer += "C: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimC[i] + " ";
                    }
                    answer += "\n";
                    answer += "A: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimA[i] + " ";
                    }
                    answer += "\n";
                    System.out.println(answer);
                    answer = "";

                    answer += "Суммарные ценности:\n";
                    answer += "По критерию A: A - " + (A[numMostValueA] + trimA[numA]) + " B - " + (A[numMostValueB] + trimA[numMostTrimB]) + " C - " + (A[numC] + trimA[numSecondTrimC]) + "\n";
                    answer += "По критерию B: A - " + (B[numMostValueA] + trimB[numA]) + " B - " + (B[numMostValueB] + trimB[numMostTrimB]) + " C - " + (B[numC] + trimB[numSecondTrimC]) + "\n";
                    answer += "По критерию C: A - " + (C[numMostValueA] + trimC[numA]) + " B - " + (C[numMostValueB] + trimC[numMostTrimB]) + " C - " + (C[numC] + trimC[numSecondTrimC]) + "\n";
                    System.out.println(answer);
                    answer = "";
                }
                // иначе все просто
                else {
                    while (numA == numMostTrimB || numA == numMostTrimC)
                        numA++;
                    answer += "B выбрал " + (numMostTrimB + 1) + " часть\n";
                    answer += "C выбрал " + (numMostTrimC + 1) + " часть\n";
                    answer += "A выбрал " + (numA + 1) + " часть\n";
                    answer += "Ценности остатка:\n";
                    answer += "B: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimB[i] + " ";
                    }
                    answer += "\n";
                    answer += "C: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimC[i] + " ";
                    }
                    answer += "\n";
                    answer += "A: ";
                    for (int i = 0; i < 3; i++) {
                        answer += trimA[i] + " ";
                    }
                    answer += "\n";
                    System.out.println(answer);
                    answer = "";

                    answer += "Суммарные ценности:\n";
                    answer += "По критерию A: A - " + (A[numMostValueA] + trimA[numA]) + " B - " + (A[numMostValueB] + trimA[numMostTrimB]) + " C - " + (A[numC] + trimA[numMostTrimC]) + "\n";
                    answer += "По критерию B: A - " + (B[numMostValueA] + trimB[numA]) + " B - " + (B[numMostValueB] + trimB[numMostTrimB]) + " C - " + (B[numC] + trimB[numMostTrimC]) + "\n";
                    answer += "По критерию C: A - " + (C[numMostValueA] + trimC[numA]) + " B - " + (C[numMostValueB] + trimC[numMostTrimB]) + " C - " + (C[numC] + trimC[numMostTrimC]) + "\n";
                    System.out.println(answer);
                    answer = "";
                }
            }
        }
    }
}
