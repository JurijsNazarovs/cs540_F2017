import numpy as np
from itertools import permutations


def NextState(state):
    ind = state.index(0)
    if ind == 0:  # LU
        return[(state[2], state[1], state[ind], state[3]),  # up
               (state[1], state[ind], state[2], state[3])]  # down
    if ind == 1:
        return[(state[0], state[3], state[2], state[ind]),  # up
               (state[ind], state[0], state[2], state[3])]  # right
    if ind == 2:
        return[(state[ind], state[1], state[0], state[3]),  # down
               (state[0], state[1], state[3], state[ind])]  # left
    if ind == 3:
        return[(state[0], state[ind], state[2], state[1]),  # down
               (state[0], state[1], state[ind], state[2])]  # right


# Describe all possible states, where 0 = space
allStates = list(permutations(range(0, 4)))

# Create Adjustent matrix
graphMatrix = np.zeros((len(allStates), len(allStates)))
for i in range(0, len(allStates)):
    for nextState in NextState(allStates[i]):
        nextStateIndex = allStates.index(nextState)
        graphMatrix[i, nextStateIndex] = 1

# Create Right Labels
labels = ["%s %s\n%s %s" % x for x in allStates]
labels = {x: labels[x].replace('0', ' ') for x in range(0, len(labels))}

# Plot
import matplotlib.pyplot as plt
import networkx as nx


def ShowGraphWithLabels(adjacency_matrix, myLabels):
    rows, cols = np.where(adjacency_matrix == 1)
    edges = zip(rows.tolist(), cols.tolist())

    plt.figure(figsize=(15, 15))
    gr = nx.Graph()
    gr.add_edges_from(edges)

    #pos = nx.spring_layout(gr)
    #nx.draw_networkx_labels(gr, pos, labels)
    #nx.draw_networkx_nodes(gr, pos, node_size=10, node_color = "red")
    #nx.draw_networkx_edges(gr, pos)
    nx.draw(gr, node_size=1000, node_color="black", alpha=0.3,
            with_labels=True, labels=myLabels, font_size=8,
            overlap=False)
    # plt.show()
    plt.savefig("graphMatrix.pdf")


ShowGraphWithLabels(graphMatrix, labels)
