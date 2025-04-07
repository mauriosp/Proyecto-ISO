export function parseNumber(input: string): string {
    if (typeof input !== 'string' || input === '') {
        return "0";
    }
    const result = input.replace(/^0+/, '');
    return result || "0";
}