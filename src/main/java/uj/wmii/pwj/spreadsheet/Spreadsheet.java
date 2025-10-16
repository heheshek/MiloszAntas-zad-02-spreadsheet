package uj.wmii.pwj.spreadsheet;

public class Spreadsheet {

    public String[][] calculate(String[][] input) {
        int height = input.length;
        int width = input[0].length;

        String[][] result = new String[height][];
        for ( int i = 0; i < height; i++ ) {
            result[i] = new String[width];
            for ( int j = 0; j < width; j++ ) {
                result[i][j] = evaluateCell(input, i, j);
            }
        }

        return result;
    }

    public String evaluateCell(String[][] input, int row, int column) {
        String source = input[row][column];

        if (source.startsWith("=")) {
            String formula = source.substring(1, 4);
            int coma = source.indexOf(',');
            int endBracket = source.indexOf(')');

            String v1 = source.substring( 5, coma );
            String v2 = source.substring( coma+1, endBracket );

            return getCellValue( input, formula, v1, v2 );
        }
        else
        if (source.startsWith("$")) {
            int[] cellID = getCellID( source );
            return evaluateCell(input, cellID[0], cellID[1] );
        }
        else {
            return source;
        }
    }

    public int[] getCellID(String source) {
        // assumes established cell id writing, e.g. "$B3"
        int[] result = new int[2];
        result[1] = 0;

        for ( int i = 1; i < source.length(); i++ ) {
            if ( source.charAt(i) < 'A' || source.charAt(i) > 'Z' ) {
                result[0] = Integer.parseInt( source.substring(i) ) - 1;
                for ( int j = 1; j < i; j++ ) {
                    result[1] *= 26;
                    result[1] += ( source.charAt(j) - 'A' + 1);
                }
                result[1] -= 1;
                break;
            }
        }
        return result;
    }

    String getCellValue(String[][] input, String formula, String v1, String v2) {
        int a, b;

        if ( v1.charAt(0) == '$' ) {
            int[] id = getCellID( v1 );
            a = Integer.parseInt( evaluateCell(input, id[0], id[1] ) );
        } else a = Integer.parseInt( v1 );

        if ( v2.charAt(0) == '$' ) {
            int[] id = getCellID( v2 );
            b = Integer.parseInt( evaluateCell(input, id[0], id[1] ) );
        } else b = Integer.parseInt( v2 );

        switch ( formula ) {
            // add sub mul div mod
            case "ADD":
                return Integer.toString( a + b );
            case "SUB":
                return Integer.toString( a - b );
            case "MUL":
                return Integer.toString( a * b );
            case "DIV":
                return Integer.toString( a / b );
            case "MOD":
                return Integer.toString( a % b );
            default:
                throw new IllegalArgumentException();
        }

    }
}
