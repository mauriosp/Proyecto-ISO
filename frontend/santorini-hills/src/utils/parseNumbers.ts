export function parseNumber(input: string): string {
    if (typeof input !== 'string' || input === '') {
        return "0";
    }
    const result = input.replace(/^0+/, '');
    return result || "0";
}

export function formatNumber(input: number): string {
    if (typeof input !== 'number' || isNaN(input)) {
        return "0";
    }
    const str = input.toString();
    let result = "";
    let count = 0;
    for (let i = str.length - 1; i >= 0; i--) {
        result = str[i] + result;
        count++;
        if (count % 3 === 0 && i !== 0) {
            if (count % 6 === 0) {
                result = "'" + result;
            } else {
                result = "." + result;
            }
        }
    }
    return result;
}