import numpy as np
import knapsacksolver
import math

np.set_printoptions(threshold=np.nan)


class FPTASSolver(knapsacksolver.KnapsackSolver):
    def __init__(self, data, capacity, max_rel_err=0, name=""):
        super().__init__(data, capacity)
        self.items_count = len(self.data_raw)
        self.name = name

        # Get rid of some bits
        if max_rel_err == 0:
            self.delete_bits = 0
        else:
            max_value = max([item[0] for item in self.data_raw])
            self.delete_bits = math.floor(math.log(max_rel_err * max_value / float(self.items_count)))
        self.data_raw = [(int(item[0] // (2 ** self.delete_bits)), item[1]) for item in self.data_raw]

        values_sum = 0
        for item in self.data_raw:
            values_sum += item[0]
        self.values_sum = values_sum

        # print("   New values: {}".format(self.data_raw))

        self.matrix = np.zeros(shape=(values_sum + 1, self.items_count + 1,))
        self.init_matrix()

        # print(self.matrix.shape)
        # print(self.matrix)

    def init_matrix(self):
        # for idx, item in enumerate(self.data_raw):
        #     self.matrix[0, idx + 1] = item[1]
        #     self.matrix[1, idx + 1] = item[0]

        for i in range(1, self.values_sum + 1):
            self.matrix[i, 0] = -1

    def solve_bag(self):
        # print("({}) fptas items: {}".format(self.name, (self.items_count + 1) * (self.values_sum + 1)))

        for i in range(1, self.items_count + 1):
            for j in range(1, self.values_sum + 1):
                # print("Calculating [{}, {}]".format(j, i))
                # W(i, j) = min (
                #                 W(i-1, j),                             <-- a
                #                 W(i-1, j - this_value) + this_weight   <-- b
                #                )

                possible_values = []

                # Calculate a
                a = self.matrix[j, i - 1]
                if a != -1:
                    possible_values.append(a)
                    # print("  Possible value a: {}".format(a))
                else:
                    pass
                    # print("  a not possible.")

                # Calculate b
                #   indexing in data_raw starts from 0, but in loop from 1 -> i-1
                this_value = self.data_raw[i - 1][0]
                this_weight = self.data_raw[i - 1][1]

                j_idx = j - this_value
                if j_idx >= 0:
                    w_value = self.matrix[j_idx, i - 1]
                    # print("self.matrix [{}, {}]".format(j_idx, i - 1))
                    if w_value != -1:
                        b = w_value + this_weight
                        possible_values.append(b)
                        # print("  Possible value b: {}".format(b))

                if possible_values:
                    new_value = min(possible_values)
                    self.matrix[j, i] = new_value
                    # print("  Writing {}".format(new_value))
                else:
                    self.matrix[j, i] = -1
                    # print("  No possible values, writing -1.")
                    pass

        # Now just go through last column in matrix and find best solution that fits in the bag.
        # print(self.matrix)

        for i in range(self.values_sum, -1, -1):
            val = self.matrix[i, self.items_count]
            if -1 < val <= self.capacity:
                return i
