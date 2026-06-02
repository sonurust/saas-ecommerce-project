export interface Order {
    id?: string;
    skuCode: string;
    quantity: number;
    price?: number;
    userDetails?: {
        username: string;
        email: string;
        name: string;
    }
}
