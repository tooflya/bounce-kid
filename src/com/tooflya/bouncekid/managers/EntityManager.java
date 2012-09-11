package com.tooflya.bouncekid.managers;

import com.tooflya.bouncekid.entity.Entity;

/**
 * @author Tooflya.com
 * @since
 */
public class EntityManager {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int count;
	protected int capacity;

	protected Entity[] elements;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityManager(final int capacity, Entity element) {
		this.count = -1;

		this.capacity = capacity;

		elements = new Entity[capacity];

		for (int i = elements.length - 1; i > 0; --i) {
			elements[i] = element.deepCopy();
			elements[i].setManager(this);
			elements[i].setID(i);
		}

		elements[0] = element;
		elements[0].setManager(this);
		elements[0].setID(0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int getCount() {
		return this.count + 1;
	}

	public int getCapacity() {
		return capacity;
	}

	public Entity getByIndex(final int index) {
		return elements[index];
	}

	public Entity create() throws NullPointerException {
		if (count + 1 < capacity) {
			count++;

			elements[count].create();

			return elements[count];
		}
		
		return null;
	}

	public void destroy(final int i) {
		Entity temp_element = elements[i];
		elements[i] = elements[count];
		elements[count] = temp_element;

		elements[i].setID(i);
		elements[count].setID(count);

		count--;
	}

	public void clear() {
		this.count = -1;

		for (int i = 0; i < elements.length; ++i) {
			elements[i].reset();
			elements[i].setVisible(false);
		}
	}
}
