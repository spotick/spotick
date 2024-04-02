const filterModal = document.getElementById('filterModal');
const checkboxes = document.querySelectorAll('.checkBox');
const areaButtons = document.querySelectorAll('.AreaGroupBtn');
const areaContainers = document.querySelectorAll('.AreasCityContainer');

document.getElementById('filterBtn').addEventListener('click', () => {
    filterModal.classList.add("On");
});

document.getElementById('filterModalClose').addEventListener('click', () => {
    filterModal.classList.remove("On");
});

document.getElementById('filterResetBtn').addEventListener('click', resetCheckboxes);

areaButtons.forEach(button => {
    button.addEventListener('click', () => {
        const target = button.getAttribute('data-target');

        button.classList.add('On');

        areaButtons.forEach(otherButton => {
            if (otherButton !== button) {
                otherButton.classList.remove('On');
            }
        });

        areaContainers.forEach(container => {
            if (container.id === target) {
                container.classList.add('On');
            } else {
                container.classList.remove('On');
            }
        });

        resetCheckboxes();
    });
});

//////////////////////////////////////////////////////////////
function resetCheckboxes() {
    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });
}