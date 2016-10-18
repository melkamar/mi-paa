import knapsacksolver


def print_rec(txt, indent=0):
    print("{}{}".format("  " * indent, txt))


class BBSolver(knapsacksolver.KnapsackSolver):
    def __init__(self, data, capacity):
        super().__init__(data, capacity)

        # pre-count remaining sums
        self.value_sums = []
        value_sum = 0
        for idx in reversed(range(0, len(self.data_raw))):
            value_sum += self.data_raw[idx][0]
            self.value_sums.insert(0, value_sum)

            # print("capacity: {}".format(capacity))
            # print("data: {}".format(self.data_raw))
            # print("value_sums: {}".format(self.value_sums))

    def solve_bag(self):
        return self.solve_bag_brute()

    def solve_bag_brute(self):
        return self.rec_brute(0, 0, 0, 0)

    def rec_brute(self, cur_weight, cur_value, start_index, best_val, indent=0):
        # print_rec("rec: weight={}  val={}  idx={}".format(cur_weight, cur_value, start_index), indent)

        if start_index == len(self.data_raw):
            return cur_value

        if self.value_sums[start_index] + cur_value < best_val:
            # print("Impossible to get more than {} with "
            #       "cur_val={} and value_sums={} (idx {})".format(best_val, cur_value, self.value_sums[start_index],
            #                                                      start_index))
            return cur_value

        for i in range(start_index, len(self.data_raw)):
            temp_weight = cur_weight + self.data_raw[i][1]

            if temp_weight > self.capacity:
                best_val = max(cur_value, best_val)
            else:
                temp_value = cur_value + self.data_raw[i][0]
                best_val = max(
                    self.rec_brute(temp_weight, temp_value, i + 1, best_val, indent + 1), best_val)

        return best_val
