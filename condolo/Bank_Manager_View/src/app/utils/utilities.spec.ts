describe('Utility Functions', () => {
  
  describe('Date Handling', () => {
    it('should create and format dates correctly', () => {
      const testDate = new Date('2025-09-29T10:30:00.000Z');
      expect(testDate).toBeInstanceOf(Date);
      expect(testDate.getFullYear()).toBe(2025);
      expect(testDate.getMonth()).toBe(8); // Septiembre es mes 8 (0-indexado)
      expect(testDate.getDate()).toBe(29);
    });

    it('should handle ISO date string formatting', () => {
      const date = new Date('2025-09-29T10:30:00.000Z');
      const isoString = date.toISOString();
      expect(isoString).toContain('2025-09-29T10:30:00');
      expect(typeof isoString).toBe('string');
    });

    it('should create dates from ISO strings', () => {
      const isoString = '2025-09-29T10:30:00';
      const date = new Date(isoString);
      expect(date).toBeInstanceOf(Date);
      expect(date.getFullYear()).toBe(2025);
    });
  });

  describe('Array Operations', () => {
    it('should filter arrays correctly', () => {
      const numbers = [1, 2, 3, 4, 5];
      const evenNumbers = numbers.filter(n => n % 2 === 0);
      expect(evenNumbers).toEqual([2, 4]);
      expect(evenNumbers.length).toBe(2);
    });

    it('should map arrays correctly', () => {
      const numbers = [1, 2, 3];
      const doubled = numbers.map(n => n * 2);
      expect(doubled).toEqual([2, 4, 6]);
      expect(doubled.length).toBe(3);
    });

    it('should reduce arrays correctly', () => {
      const numbers = [1, 2, 3, 4, 5];
      const sum = numbers.reduce((acc, num) => acc + num, 0);
      expect(sum).toBe(15);
      expect(typeof sum).toBe('number');
    });
  });

  describe('Object Operations', () => {
    it('should handle object property access', () => {
      const testObject = {
        id: 1,
        name: 'Test Object',
        active: true,
        balance: 100.50
      };

      expect(testObject.id).toBe(1);
      expect(testObject.name).toBe('Test Object');
      expect(testObject.active).toBe(true);
      expect(testObject.balance).toBe(100.50);
    });

    it('should handle object destructuring', () => {
      const customer = {
        customerId: 'CUST001',
        name: 'John Doe',
        status: 'A'
      };

      const { customerId, name, status } = customer;
      
      expect(customerId).toBe('CUST001');
      expect(name).toBe('John Doe');
      expect(status).toBe('A');
    });

    it('should handle object spread operations', () => {
      const baseObject = { a: 1, b: 2 };
      const extendedObject = { ...baseObject, c: 3 };

      expect(extendedObject.a).toBe(1);
      expect(extendedObject.b).toBe(2);
      expect(extendedObject.c).toBe(3);
      expect(Object.keys(extendedObject)).toEqual(['a', 'b', 'c']);
    });
  });

  describe('String Operations', () => {
    it('should handle string manipulations', () => {
      const text = 'Hello World';
      expect(text.toLowerCase()).toBe('hello world');
      expect(text.toUpperCase()).toBe('HELLO WORLD');
      expect(text.length).toBe(11);
    });

    it('should handle string splitting', () => {
      const csv = 'name,age,city';
      const parts = csv.split(',');
      expect(parts).toEqual(['name', 'age', 'city']);
      expect(parts.length).toBe(3);
    });

    it('should handle template literals', () => {
      const name = 'Angular';
      const version = 17;
      const message = `Using ${name} version ${version}`;
      
      expect(message).toBe('Using Angular version 17');
      expect(message).toContain('Angular');
      expect(message).toContain('17');
    });
  });

  describe('Number Operations', () => {
    it('should handle number calculations', () => {
      expect(2 + 2).toBe(4);
      expect(10 - 3).toBe(7);
      expect(5 * 4).toBe(20);
      expect(15 / 3).toBe(5);
    });

    it('should handle floating point numbers', () => {
      const balance = 1000.50;
      const deposit = 250.25;
      const newBalance = balance + deposit;
      
      expect(newBalance).toBe(1250.75);
      expect(typeof newBalance).toBe('number');
    });

    it('should handle number formatting', () => {
      const amount = 1234.567;
      expect(amount.toFixed(2)).toBe('1234.57');
      expect(parseFloat(amount.toFixed(2))).toBe(1234.57);
    });
  });

  describe('Type Checking', () => {
    it('should correctly identify types', () => {
      expect(typeof 'string').toBe('string');
      expect(typeof 123).toBe('number');
      expect(typeof true).toBe('boolean');
      expect(typeof {}).toBe('object');
      expect(typeof []).toBe('object');
      expect(Array.isArray([])).toBe(true);
      expect(Array.isArray({})).toBe(false);
    });

    it('should handle null and undefined', () => {
      expect(null).toBeNull();
      expect(undefined).toBeUndefined();
      expect(typeof null).toBe('object');
      expect(typeof undefined).toBe('undefined');
    });
  });

  describe('Boolean Logic', () => {
    it('should handle boolean operations', () => {
      expect(true && true).toBe(true);
      expect(true && false).toBe(false);
      expect(true || false).toBe(true);
      expect(false || false).toBe(false);
      expect(!true).toBe(false);
      expect(!false).toBe(true);
    });

    it('should handle truthy and falsy values', () => {
      expect(Boolean('')).toBe(false);
      expect(Boolean('text')).toBe(true);
      expect(Boolean(0)).toBe(false);
      expect(Boolean(1)).toBe(true);
      expect(Boolean(null)).toBe(false);
      expect(Boolean(undefined)).toBe(false);
    });
  });

  describe('Error Handling', () => {
    it('should handle try-catch blocks', () => {
      let error: Error | null = null;
      
      try {
        throw new Error('Test error');
      } catch (e) {
        error = e as Error;
      }
      
      expect(error).toBeInstanceOf(Error);
      expect(error?.message).toBe('Test error');
    });

    it('should validate function parameters', () => {
      const divide = (a: number, b: number): number => {
        if (b === 0) {
          throw new Error('Division by zero');
        }
        return a / b;
      };

      expect(divide(10, 2)).toBe(5);
      expect(() => divide(10, 0)).toThrow('Division by zero');
    });
  });
});